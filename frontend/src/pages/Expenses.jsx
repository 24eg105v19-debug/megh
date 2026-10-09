import React, { useState, useEffect } from 'react'
import { format } from 'date-fns'
import api from '../api'
import Navbar from '../components/Navbar'

const RANGE = { start: '2000-01-01', end: '2100-12-31' }

export default function Expenses() {
  const [expenses, setExpenses] = useState([])
  const [categories, setCategories] = useState([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [formData, setFormData] = useState({
    amount: '',
    categoryId: '',
    description: '',
    expenseDate: format(new Date(), 'yyyy-MM-dd')
  })
  const [errors, setErrors] = useState({})
  const [submitting, setSubmitting] = useState(false)

  useEffect(() => {
    loadCategories()
    fetchExpenses()
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

  const fetchExpenses = async () => {
    try {
      const response = await api.get('/expenses', { params: RANGE })
      setExpenses(response.data)
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
    if (!formData.amount || parseFloat(formData.amount) <= 0) newErrors.amount = 'Valid amount required'
    if (!formData.categoryId) newErrors.categoryId = 'Category required'
    if (!formData.expenseDate) newErrors.expenseDate = 'Date required'
    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!validate()) return
    setSubmitting(true)
    try {
      await api.post('/expenses', {
        categoryId: parseInt(formData.categoryId, 10),
        amount: parseFloat(formData.amount),
        description: formData.description,
        expenseDate: formData.expenseDate
      })
      setShowForm(false)
      setFormData({ amount: '', categoryId: categories[0] ? String(categories[0].id) : '', description: '', expenseDate: format(new Date(), 'yyyy-MM-dd') })
      fetchExpenses()
    } catch (err) {
      setErrors({ form: err.response?.data?.message || 'Failed to add expense' })
    } finally {
      setSubmitting(false)
    }
  }

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this expense?')) return
    try {
      await api.delete(`/expenses/${id}`)
      fetchExpenses()
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
          <h1 className="dashboard-title">Expenses</h1>
          <button onClick={() => setShowForm(!showForm)} className="btn btn-primary">
            {showForm ? 'Cancel' : 'Add Expense'}
          </button>
        </div>

        {showForm && (
          <div className="card" style={{marginBottom: '2rem'}}>
            <h2 className="section-title" style={{marginBottom: '1rem'}}>Add New Expense</h2>
            {errors.form && <div className="form-error" style={{marginBottom: '1rem'}}>{errors.form}</div>}
            <form onSubmit={handleSubmit} className="expense-form">
              <div className="form-group">
                <label className="form-label" htmlFor="amount">Amount</label>
                <input
                  type="number"
                  id="amount"
                  name="amount"
                  className="form-input"
                  value={formData.amount}
                  onChange={handleChange}
                  placeholder="0.00"
                  step="0.01"
                  min="0.01"
                  required
                />
                {errors.amount && <p className="form-error">{errors.amount}</p>}
              </div>
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
                <label className="form-label" htmlFor="description">Description (optional)</label>
                <input
                  type="text"
                  id="description"
                  name="description"
                  className="form-input"
                  value={formData.description}
                  onChange={handleChange}
                  placeholder="Lunch with friends"
                />
              </div>
              <div className="form-group">
                <label className="form-label" htmlFor="expenseDate">Date</label>
                <input
                  type="date"
                  id="expenseDate"
                  name="expenseDate"
                  className="form-input"
                  value={formData.expenseDate}
                  onChange={handleChange}
                  required
                />
              </div>
              <div style={{gridColumn: '1 / -1', display: 'flex', gap: '0.5rem', justifyContent: 'flex-end'}}>
                <button type="button" className="btn btn-secondary" onClick={() => setShowForm(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary" disabled={submitting}>
                  {submitting ? 'Adding...' : 'Add Expense'}
                </button>
              </div>
            </form>
          </div>
        )}

        <div className="card">
          <h2 className="section-title">All Expenses</h2>
          {loading ? (
            <div style={{textAlign: 'center', padding: '2rem', color: '#6b7280'}}>Loading...</div>
          ) : expenses.length === 0 ? (
            <div style={{textAlign: 'center', padding: '3rem', color: '#9ca3af'}}>
              No expenses yet. Click "Add Expense" to get started!
            </div>
          ) : (
            <div className="expense-list">
              {expenses.map(expense => (
                <div key={expense.id} className="expense-item">
                  <div className="expense-info">
                    <span className="expense-category">{expense.categoryName}</span>
                    {expense.description && <span className="expense-description">{expense.description}</span>}
                    <span className="expense-date">{format(new Date(expense.expenseDate), 'MMM d, yyyy')}</span>
                  </div>
                  <div style={{display: 'flex', alignItems: 'center', gap: '1rem'}}>
                    <span className="expense-amount">{formatCurrency(expense.amount)}</span>
                    <button onClick={() => handleDelete(expense.id)} className="btn btn-danger" style={{padding: '0.375rem 0.75rem', fontSize: '0.75rem'}}>
                      Delete
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </main>
    </div>
  )
}
