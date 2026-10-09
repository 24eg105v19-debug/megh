import React, { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement,
  PointElement,
  LineElement
} from 'chart.js'
import { Bar, Doughnut } from 'react-chartjs-2'
import api from '../api'
import Navbar from '../components/Navbar'
import { format } from 'date-fns'

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement,
  PointElement,
  LineElement
)

const CATEGORIES = ['Food', 'Transport', 'Entertainment', 'Shopping', 'Bills', 'Health', 'Education', 'Other']

export default function Dashboard() {
  const [summary, setSummary] = useState(null)
  const [loading, setLoading] = useState(true)
  const [income, setIncome] = useState('')
  const [showIncomeModal, setShowIncomeModal] = useState(false)
  const [savingIncome, setSavingIncome] = useState(false)
  const navigate = useNavigate()

  useEffect(() => {
    fetchDashboard()
  }, [])

  const fetchDashboard = async () => {
    try {
      const response = await api.get('/dashboard')
      setSummary(response.data)
    } catch (err) {
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  const handleIncomeSubmit = async (e) => {
    e.preventDefault()
    if (!income) return
    setSavingIncome(true)
    try {
      await api.put('/income', { monthlyIncome: parseFloat(income) })
      setShowIncomeModal(false)
      fetchDashboard()
    } catch (err) {
      console.error(err)
    } finally {
      setSavingIncome(false)
    }
  }

  const formatCurrency = (value) => {
    if (!value) return '$0.00'
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 2
    }).format(value)
  }

  if (loading) {
    return (
      <div className="container" style={{paddingTop: '3rem'}}>
        <div style={{textAlign: 'center'}}>
          <div style={{fontSize: '1.5rem', color: '#6b7280'}}>Loading dashboard...</div>
        </div>
      </div>
    )
  }

  const totalIncome = summary?.totalIncome || 0
  const totalExpenses = summary?.totalExpenses || 0
  const remainingBudget = summary?.remainingBudget || 0
  const expensesByCategory = summary?.expensesByCategory || {}
  const budgetStatuses = summary?.budgetStatuses || []
  const savingsTips = summary?.savingsTips || []

  const categoryData = {
    labels: Object.keys(expensesByCategory),
    datasets: [{
      label: 'Expenses by Category',
      data: Object.values(expensesByCategory).map(v => parseFloat(v)),
      backgroundColor: [
        '#4f46e5', '#10b981', '#f59e0b', '#ef4444',
        '#8b5cf6', '#ec4899', '#06b6d4', '#6b7280'
      ],
      borderRadius: 8,
    }]
  }

  const budgetData = {
    labels: budgetStatuses.map(b => b.category),
    datasets: [{
      label: 'Budget Used (%)',
      data: budgetStatuses.map(b => b.percentageUsed || 0),
      backgroundColor: budgetStatuses.map(b => 
        b.isOverBudget ? '#ef4444' : b.percentageUsed > 80 ? '#f59e0b' : '#10b981'
      ),
      borderRadius: 8,
    }]
  }

  return (
    <div>
      <Navbar />
      <main className="container" style={{padding: '2rem 1rem'}}>
        <div className="dashboard-header">
          <h1 className="dashboard-title">Dashboard</h1>
          <button onClick={() => setShowIncomeModal(true)} className="btn btn-primary">
            Set Monthly Income
          </button>
        </div>

        <div className="stats-grid">
          <div className="stat-card">
            <div className="stat-label">Monthly Income</div>
            <div className="stat-value positive">{formatCurrency(totalIncome)}</div>
          </div>
          <div className="stat-card">
            <div className="stat-label">Total Expenses</div>
            <div className="stat-value negative">{formatCurrency(totalExpenses)}</div>
          </div>
          <div className="stat-card">
            <div className="stat-label">Remaining Budget</div>
            <div className={`stat-value ${remainingBudget >= 0 ? 'positive' : 'negative'}`}>
              {formatCurrency(remainingBudget)}
            </div>
          </div>
          <div className="stat-card">
            <div className="stat-label">Savings Rate</div>
            <div className="stat-value positive">
              {totalIncome > 0 ? `${(((totalIncome - totalExpenses) / totalIncome) * 100).toFixed(1)}%` : '0%'}
            </div>
          </div>
        </div>

        <div style={{display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(400px, 1fr))', gap: '1.5rem'}}>
          <div className="card">
            <h2 className="section-title">Expenses by Category</h2>
            <div className="chart-container">
              {Object.keys(expensesByCategory).length > 0 ? (
                <Doughnut data={categoryData} options={{responsive: true, maintainAspectRatio: false}} />
              ) : (
                <div style={{height: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#9ca3af'}}>
                  No expenses recorded yet
                </div>
              )}
            </div>
          </div>

          <div className="card">
            <h2 className="section-title">Budget Progress</h2>
            <div className="chart-container">
              {budgetStatuses.length > 0 ? (
                <Bar data={budgetData} options={{responsive: true, maintainAspectRatio: false, indexAxis: 'y', scales: {x: {max: 100}}}} />
              ) : (
                <div style={{height: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#9ca3af'}}>
                  No budgets set yet
                </div>
              )}
            </div>
          </div>
        </div>

        {budgetStatuses.length > 0 && (
          <div className="card">
            <h2 className="section-title">Budget Details</h2>
            <div style={{display: 'flex', flexDirection: 'column', gap: '0.75rem'}}>
              {budgetStatuses.map(budget => (
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
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {savingsTips.length > 0 && (
          <div className="card">
            <h2 className="section-title">AI Savings Tips</h2>
            <div className="tips-list">
              {savingsTips.slice(0, 3).map(tip => (
                <div key={tip.id} className={`tip-item ${tip.isRead ? 'read' : ''}`}>
                  <div className="tip-content">
                    <div className="tip-text">
                      <div className="tip-category">{tip.category}</div>
                      {tip.tip}
                    </div>
                    <div className="tip-actions">
                      {!tip.isRead && (
                        <button 
                          onClick={() => api.put(`/tips/${tip.id}/read`)}
                          className="btn btn-outline"
                          style={{padding: '0.25rem 0.75rem', fontSize: '0.75rem'}}
                        >
                          Mark Read
                        </button>
                      )}
                    </div>
                  </div>
                </div>
              ))}
            </div>
            <div style={{textAlign: 'center', marginTop: '1rem'}}>
              <a href="/tips" className="btn btn-outline">View All Tips</a>
            </div>
          </div>
        )}
      </main>

      {showIncomeModal && (
        <div className="modal-overlay" onClick={() => setShowIncomeModal(false)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <h2 className="modal-title">Set Monthly Income</h2>
            <form onSubmit={handleIncomeSubmit}>
              <div className="form-group">
                <label className="form-label" htmlFor="income">Monthly Income</label>
                <input
                  type="number"
                  id="income"
                  name="income"
                  className="form-input"
                  value={income}
                  onChange={e => setIncome(e.target.value)}
                  placeholder="5000"
                  step="0.01"
                  min="0"
                  required
                  disabled={savingIncome}
                />
              </div>
              <div style={{display: 'flex', gap: '0.5rem', justifyContent: 'flex-end'}}>
                <button type="button" className="btn btn-secondary" onClick={() => setShowIncomeModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary" disabled={savingIncome}>
                  {savingIncome ? 'Saving...' : 'Save'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}