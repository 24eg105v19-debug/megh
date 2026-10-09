import React, { useState, useEffect } from 'react'
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

const CHART_COLORS = [
  '#4f46e5', '#10b981', '#f59e0b', '#ef4444',
  '#8b5cf6', '#ec4899', '#06b6d4', '#6b7280'
]

export default function Dashboard() {
  const [summary, setSummary] = useState(null)
  const [loading, setLoading] = useState(true)
  const [income, setIncome] = useState('')
  const [showIncomeModal, setShowIncomeModal] = useState(false)
  const [savingIncome, setSavingIncome] = useState(false)

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
      await api.put('/user/profile', null, { params: { monthlyIncome: parseFloat(income) } })
      setShowIncomeModal(false)
      setIncome('')
      fetchDashboard()
    } catch (err) {
      console.error(err)
    } finally {
      setSavingIncome(false)
    }
  }

  const formatCurrency = (value) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 2
    }).format(value || 0)
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

  const monthlyIncome = summary?.monthlyIncome || 0
  const monthlyExpenses = summary?.monthlyExpenses || 0
  const monthlyBudget = summary?.monthlyBudget || 0
  const netSavings = summary?.netSavings || 0
  const remainingBudget = monthlyBudget - monthlyExpenses
  const categorySummary = summary?.topExpenseCategories || []
  const activeBudgets = summary?.activeBudgets || []
  const recentExpenses = summary?.recentExpenses || []

  const categoryData = {
    labels: categorySummary.map(c => c.categoryName),
    datasets: [{
      label: 'Expenses by Category',
      data: categorySummary.map(c => parseFloat(c.totalAmount || 0)),
      backgroundColor: categorySummary.map((c, i) => c.color || CHART_COLORS[i % CHART_COLORS.length]),
      borderRadius: 8,
    }]
  }

  const budgetData = {
    labels: activeBudgets.map(b => b.categoryName),
    datasets: [{
      label: 'Budget Used (%)',
      data: activeBudgets.map(b => Math.min(b.progressPercentage || 0, 100)),
      backgroundColor: activeBudgets.map(b =>
        (b.progressPercentage || 0) > 100 ? '#ef4444' : (b.progressPercentage || 0) > 80 ? '#f59e0b' : '#10b981'
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
            <div className="stat-value positive">{formatCurrency(monthlyIncome)}</div>
          </div>
          <div className="stat-card">
            <div className="stat-label">Monthly Expenses</div>
            <div className="stat-value negative">{formatCurrency(monthlyExpenses)}</div>
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
              {monthlyIncome > 0 ? `${((netSavings / monthlyIncome) * 100).toFixed(1)}%` : '0%'}
            </div>
          </div>
        </div>

        <div style={{display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(400px, 1fr))', gap: '1.5rem'}}>
          <div className="card">
            <h2 className="section-title">Expenses by Category</h2>
            <div className="chart-container">
              {categorySummary.length > 0 ? (
                <Doughnut data={categoryData} options={{responsive: true, maintainAspectRatio: false}} />
              ) : (
                <div style={{height: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#9ca3af'}}>
                  No expenses recorded this month
                </div>
              )}
            </div>
          </div>

          <div className="card">
            <h2 className="section-title">Budget Progress</h2>
            <div className="chart-container">
              {activeBudgets.length > 0 ? (
                <Bar data={budgetData} options={{responsive: true, maintainAspectRatio: false, indexAxis: 'y', scales: {x: {max: 100}}}} />
              ) : (
                <div style={{height: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#9ca3af'}}>
                  No budgets set yet
                </div>
              )}
            </div>
          </div>
        </div>

        {activeBudgets.length > 0 && (
          <div className="card">
            <h2 className="section-title">Budget Details</h2>
            <div style={{display: 'flex', flexDirection: 'column', gap: '0.75rem'}}>
              {activeBudgets.map(budget => {
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
                    </div>
                  </div>
                )
              })}
            </div>
          </div>
        )}

        {recentExpenses.length > 0 && (
          <div className="card">
            <h2 className="section-title">Recent Expenses</h2>
            <div className="expense-list">
              {recentExpenses.map(expense => (
                <div key={expense.id} className="expense-item">
                  <div className="expense-info">
                    <span className="expense-category">{expense.categoryName}</span>
                    {expense.description && <span className="expense-description">{expense.description}</span>}
                    <span className="expense-date">{format(new Date(expense.expenseDate), 'MMM d, yyyy')}</span>
                  </div>
                  <span className="expense-amount">{formatCurrency(expense.amount)}</span>
                </div>
              ))}
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
