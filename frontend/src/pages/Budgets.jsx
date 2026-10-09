import React, { useState, useEffect } from 'react'
import { format } from 'date-fns'
import api from '../api'
import Navbar from '../components/Navbar'

export default function Budgets() {
  const [budgets, setBudgets] = useState([])
  const [categories, setCategories] = useState([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [formData, setFormData] = useState({
    categoryId: '',
    amount: '',
    startDate: format(new Date(), 'yyyy-MM-01'),
    endDate: format(new Date(new Date().getFullYear(), new Date().getMonth() + 1, 0), 'yyyy-MM-dd')
  })
  const [errors, setErrors] = useState({})
  const [submitting, setSubmitting] = useState(false)

  useEffect(() => {
    loadCategories()
    fetchBudgets()
  }, [])

  const loadCategories = async () => {
    try {
      const response = await api.get('/categories/type/expense')
      setCategories(response.data)
      if (response.data.length > 0) {
        setFormData(prev => prev.categoryId ? prev : { ...prev, categoryId: String(response.data[0].id) })
      }
    } catch (err) {
      console.error(err)
    }
  }

  const fetchBudgets = async () => {
    try {
      const response = await api.get('/budgets')
      setBudgets(response.data)
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
    if (!formData.categoryId) newErrors.categoryId = 'Category required'
    if (!formData.amount || parseFloat(formData.amount) <= 0) newErrors.amount = 'Valid amount required'
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
        categoryId: parseInt(formData.categoryId, 10),
        amount: parseFloat(formData.amount),
        startDate: formData.startDate,
        endDate: formData.endDate
      })
      setShowForm(false)
      setFormData({
        categoryId: categories[0] ? String(categories[0].id) : '',
        amount: '',
        startDate: format(new Date(), 'yyyy-MM-01'),
        endDate: format(new Date(new Date().getFullYear(), new Date().getMonth() + 1, 0), 'yyyy-MM-dd')
      })
      fetchBudgets()
    } catch (err) {
      setErrors({ form: err.response?.data?.message || 'Failed to create budget' })
    } finally {
      setSubmitting(false)
    }
  }

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this budget?')) return
    try {
      await api.delete(`/budgets/${id}`)
      fetchBudgets()
    } catch (err) {
      console.error(err)
    }
  }

  const formatCurrency = (value) => {
    return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(value || 0)
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
            {errors.form && <div className="form-error" style={{marginBottom: '1rem'}}>{errors.form}</div>}
            <form onSubmit={handleSubmit} className="expense-form">
              <div className="form-group">
                <label className="form-label" htmlFor="categoryId">Category</label>
                <select
                  id="categoryId"
                  name="categoryId"
                  className="form-input"
                  value={formData.categoryId}
                  onChange={handleChange}
                  required
                >
                  {categories.map(cat => <option key={cat.id} value={cat.id}>{cat.name}</option>)}
                </select>
                {errors.categoryId && <p className="form-error">{errors.categoryId}</p>}
              </div>
              <div className="form-group">
                <label className="form-label" htmlFor="amount">Limit Amount</label>
                <input
                  type="number"
                  id="amount"
                  name="amount"
                  className="form-input"
                  value={formData.amount}
                  onChange={handleChange}
                  placeholder="500.00"
                  step="0.01"
                  min="0.01"
                  required
                />
                {errors.amount && <p className="form-error">{errors.amount}</p>}
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
          ) : budgets.length === 0 ? (
            <div style={{textAlign: 'center', padding: '3rem', color: '#9ca3af'}}>
              No budgets set yet. Click "Create Budget" to get started!
            </div>
          ) : (
            <div style={{display: 'flex', flexDirection: 'column', gap: '0.75rem'}}>
              {budgets.map(budget => {
                const over = (budget.progressPercentage || 0) > 100
                return (
                  <div key={budget.id} className="budget-item">
                    <div className="budget-header">
                      <span className="budget-category">{budget.categoryName}</span>
                      <span style={{fontWeight: 600, color: over ? '#ef4444' : '#374151'}}>
                        {formatCurrency(budget.spentAmount)} / {formatCurrency(budget.amount)}
                      </span>
                    </div>
                    <div className="budget-progress">
                      <div className={`budget-progress-bar ${over ? 'danger' : (budget.progressPercentage || 0) > 80 ? 'warning' : 'safe'}`}
                           style={{width: `${Math.min(budget.progressPercentage || 0, 100)}%`}}></div>
                    </div>
                    <div className="budget-details">
                      <span>{(budget.progressPercentage || 0).toFixed(1)}% used</span>
                      <span style={{color: over ? '#ef4444' : '#10b981'}}>
                        {over ? 'Over budget' : 'On track'}
                      </span>
                      <span>{formatCurrency(budget.remainingAmount)} remaining</span>
                    </div>
                  </div>
                )
              })}
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
                    <span className="budget-category">{budget.categoryName}</span>
                    <div className="budget-details">
                      <span>{formatCurrency(budget.amount)}</span>
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
