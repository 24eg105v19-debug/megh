import React, { useState, useEffect } from 'react'
import api from '../api'
import Navbar from '../components/Navbar'

export default function Tips() {
  const [tips, setTips] = useState([])
  const [loading, setLoading] = useState(true)
  const [generating, setGenerating] = useState(false)

  useEffect(() => {
    fetchTips()
  }, [])

  const fetchTips = async () => {
    try {
      const response = await api.get('/tips')
      setTips(response.data)
    } catch (err) {
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  const handleGenerate = async () => {
    setGenerating(true)
    try {
      const response = await api.post('/tips/generate')
      setTips(response.data)
    } catch (err) {
      console.error(err)
    } finally {
      setGenerating(false)
    }
  }

  const handleMarkRead = async (id) => {
    try {
      await api.put(`/tips/${id}/read`)
      setTips(prev => prev.map(tip => tip.id === id ? {...tip, isRead: true} : tip))
    } catch (err) {
      console.error(err)
    }
  }

  return (
    <div>
      <Navbar />
      <main className="container" style={{padding: '2rem 1rem'}}>
        <div className="dashboard-header">
          <h1 className="dashboard-title">AI Savings Tips</h1>
          <button onClick={handleGenerate} className="btn btn-primary" disabled={generating}>
            {generating ? 'Generating...' : 'Generate New Tips'}
          </button>
        </div>

        <div className="card">
          {loading ? (
            <div style={{textAlign: 'center', padding: '2rem', color: '#6b7280'}}>Loading...</div>
          ) : tips.length === 0 ? (
            <div style={{textAlign: 'center', padding: '3rem', color: '#9ca3af'}}>
              <div style={{fontSize: '3rem', marginBottom: '1rem'}}>💡</div>
              <p>No tips yet. Click "Generate New Tips" to get personalized savings advice!</p>
            </div>
          ) : (
            <div className="tips-list">
              {tips.map(tip => (
                <div key={tip.id} className={`tip-item ${tip.isRead ? 'read' : ''}`}>
                  <div className="tip-content">
                    <div className="tip-text">
                      <div className="tip-category">{tip.category} • Priority: {tip.priorityLevel}</div>
                      {tip.tip}
                    </div>
                    <div className="tip-actions">
                      {!tip.isRead && (
                        <button 
                          onClick={() => handleMarkRead(tip.id)}
                          className="btn btn-primary"
                          style={{padding: '0.375rem 0.75rem', fontSize: '0.75rem'}}
                        >
                          Mark Read
                        </button>
                      )}
                    </div>
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