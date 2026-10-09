import React, { useState, useEffect } from 'react'
import { format } from 'date-fns'
import api from '../api'
import Navbar from '../components/Navbar'

const CATEGORIES = ['Food', 'Transport', 'Entertainment', 'Shopping', 'Bills', 'Health', 'Education', 'Other']

export default function Budgets() {
  const [budgets, setBudgets] = useState([])
  const [statuses, setStatuses] = useState([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [formData, setFormData] = useState({
    category: 'Food',
    limitAmount: '',
    startDate: format(new Date(), 'yyyy-MM-01'),
    endDate: format(new Date(new Date().getFullYear(), new Date().getMonth() + 1, 0), 'yyyy-MM-dd')
  })
  const [errors, setErrors] = useState({})
  const [submitting, setSubmitting] = useState(false)

  useEffect(() => {
    fetchBudgets()
    fetchStatuses()
  }, [])

  const fetchBudgets = async () => {
    try {
      const response = await api.get('/budgets')
      setBudgets(response.data)
    } catch (err) {
      console.error(err)
    }
  }

  const fetchStatuses = async () => {
    try {
      const response = await api.get('/budgets/status')
      setStatuses(response.data)
    } catch (err) {
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  const handleChange = (e) => {
    const { name, value } = e.target
    setFormData(prev => ({ ...prev, [name]: value }))
    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: '' }))
    }
  }

  const validate = () => {
    const newErrors = {}
    if (!formData.category) newErrors.category = 'Category required'
    if (!formData.limitAmount || parseFloat(formData.limitAmount) <= 0) newErrors.limitAmount = 'Valid amount required'
    if (!formData.startDate) newErrors.startDate = 'Start date required'
    if (!formData.endDate) newErrors.endDate = 'End date required'
    if (formData.startDate && formData.endDate && new Date(formData.startDate) > new Date(formData.endDate)) {
      newErrors.endDate = 'End date must be after start date'
    }
    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!validate()) return
    setSubmitting(true)
    try {
      await api.post('/budgets', {
        category: formData.category,
        limitAmount: parseFloat(formData.limitAmount),
        startDate: formData.startDate,
        endDate: formData.endDate
      })
      setShowForm(false)
      setFormData({ category: 'Food', limitAmount: '', startDate: format(new Date(), 'yyyy-MM-01'), endDate: format(new Date(new Date().getFullYear(), new Date().getMonth() + 1, 0), 'yyyy-MM-dd') })
      fetchBudgets()
      fetchStatuses()
    } catch (err) {
      console.error(err)
    } finally {
      setSubmitting(false)
    }
  }

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this budget?')) return
    try {
      await api.delete(`/budgets/${id}`)
      fetchBudgets()
      fetchStatuses()
    } catch (err) {
      console.error(err)
    }
  }

  const formatCurrency = (value) => {
    return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(value)
  }

  return (
    <div>
      <Navbar />
      <main className="container" style={{padding: '2rem 1rem'}}>
        <div className="dashboard-header">
          <h1 className="dashboard-title">Budgets</h1>
          <button onClick={() => setShowForm(!showForm)} className="btn btn-primary">
            {showForm ? 'Cancel' : 'Create Budget'}
          </button>
        </div>

        {showForm && (
          <div className="card" style={{marginBottom: '2rem'}}>
            <h2 className="section-title" style={{marginBottom: '1rem'}}>Create New Budget</h2>
            <form onSubmit={handleSubmit} className="expense-form">
              <div className="form-group">
                <label className="form-label" htmlFor="category">Category</label>
                <select
                  id="category"
                  name="category"
                  className="form-input"
                  value={formData.category}
                  onChange={handleChange}
                  required
                >
                  {CATEGORIES.map(cat => <option key={cat} value={cat}>{cat}</option>)}
                </select>
              </div>
              <div className="form-group">
                <label className="form-label" htmlFor="limitAmount">Limit Amount</label>
                <input
                  type="number"
                  id="limitAmount"
                  name="limitAmount"
                  className="form-input"
                  value={formData.limitAmount}
                  onChange={handleChange}
                  placeholder="500.00"
                  step="0.01"
                  min="0.01"
                  required
                />
                {errors.limitAmount && <p className="form-error">{errors.limitAmount}</p>}
              </div>
              <div className="form-group">
                <label className="form-label" htmlFor="startDate">Start Date</label>
                <input
                  type="date"
                  id="startDate"
                  name="startDate"
                  className="form-input"
                  value={formData.startDate}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="form-group">
                <label className="form-label" htmlFor="endDate">End Date</label>
                <input
                  type="date"
                  id="endDate"
                  name="endDate"
                  className="form-input"
                  value={formData.endDate}
                  onChange={handleChange}
                  required
                />
                {errors.endDate && <p className="form-error">{errors.endDate}</p>}
              </div>
              <div style={{gridColumn: '1 / -1', display: 'flex', gap: '0.5rem', justifyContent: 'flex-end'}}>
                <button type="button" className="btn btn-secondary" onClick={() => setShowForm(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary" disabled={submitting}>
                  {submitting ? 'Creating...' : 'Create Budget'}
                </button>
              </div>
            </form>
          </div>
        )}

        <div className="card">
          <h2 className="section-title">Budget Status</h2>
          {loading ? (
            <div style={{textAlign: 'center', padding: '2rem', color: '#6b7280'}}>Loading...</div>
          ) : statuses.length === 0 ? (
            <div style={{textAlign: 'center', padding: '3rem', color: '#9ca3af'}}>
              No budgets set yet. Click "Create Budget" to get started!
            </div>
          ) : (
            <div style={{display: 'flex', flexDirection: 'column', gap: '0.75rem'}}>
              {statuses.map(budget => (
                <div key={budget.category} className="budget-item">
                  <div className="budget-header">
                    <span className="budget-category">{budget.category}</span>
                    <span style={{fontWeight: 600, color: budget.isOverBudget ? '#ef4444' : '#374151'}}>
                      {formatCurrency(budget.spentAmount)} / {formatCurrency(budget.limitAmount)}
                    </span>
                  </div>
                  <div className="budget-progress">
                    <div className={`budget-progress-bar ${budget.isOverBudget ? 'danger' : budget.percentageUsed > 80 ? 'warning' : 'safe'}`}
                         style={{width: `${Math.min(budget.percentageUsed || 0, 100)}%`}}></div>
                  </div>
                  <div className="budget-details">
                    <span>{budget.percentageUsed?.toFixed(1) || 0}% used</span>
                    <span style={{color: budget.isOverBudget ? '#ef4444' : '#10b981'}}>
                      {budget.isOverBudget ? 'Over budget' : 'On track'}
                    </span>
                    <span>{formatCurrency(budget.remainingAmount)} remaining</span>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {budgets.length > 0 && (
          <div className="card" style={{marginTop: '2rem'}}>
            <h2 className="section-title">Your Budgets</h2>
            <div style={{display: 'flex', flexDirection: 'column', gap: '0.75rem'}}>
              {budgets.map(budget => (
                <div key={budget.id} className="budget-item" style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                  <div>
                    <span className="budget-category">{budget.category}</span>
                    <div className="budget-details">
                      <span>{formatCurrency(budget.limitAmount)}</span>
                      <span>{format(new Date(budget.startDate), 'MMM d')} - {format(new Date(budget.endDate), 'MMM d, yyyy')}</span>
                    </div>
                  </div>
                  <button onClick={() => handleDelete(budget.id)} className="btn btn-danger" style={{padding: '0.375rem 0.75rem', fontSize: '0.75rem'}}>
                    Delete
                  </button>
                </div>
              ))}
            </div>
          </div>
        )}
      </main>
    </div>
  )
}