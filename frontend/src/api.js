import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_URL || '/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export const authAPI = {
  login: (data) => api.post('/auth/login', data),
  register: (data) => api.post('/auth/register', data),
};

export const categoryAPI = {
  getAll: () => api.get('/categories'),
  getByType: (type) => api.get(`/categories/type/${type}`),
};

export const expenseAPI = {
  create: (data) => api.post('/expenses', data),
  getByDateRange: (start, end) => api.get('/expenses', { params: { start, end } }),
  getByDate: (date) => api.get(`/expenses/date/${date}`),
  getTotal: (start, end) => api.get('/expenses/total', { params: { start, end } }),
  getByCategory: (start, end) => api.get('/expenses/by-category', { params: { start, end } }),
  update: (id, data) => api.put(`/expenses/${id}`, data),
  delete: (id) => api.delete(`/expenses/${id}`),
};

export const incomeAPI = {
  create: (data) => api.post('/incomes', data),
  getByDateRange: (start, end) => api.get('/incomes', { params: { start, end } }),
  getByDate: (date) => api.get(`/incomes/date/${date}`),
  getTotal: (start, end) => api.get('/incomes/total', { params: { start, end } }),
  update: (id, data) => api.put(`/incomes/${id}`, data),
  delete: (id) => api.delete(`/incomes/${id}`),
};

export const budgetAPI = {
  create: (data) => api.post('/budgets', data),
  getAll: () => api.get('/budgets'),
  getActive: (date) => api.get('/budgets/active', { params: { date } }),
  update: (id, data) => api.put(`/budgets/${id}`, data),
  delete: (id) => api.delete(`/budgets/${id}`),
};

export const dashboardAPI = {
  getSummary: () => api.get('/dashboard'),
};

export const userAPI = {
  getProfile: () => api.get('/user/profile'),
  updateProfile: (data) => api.put('/user/profile', null, { params: data }),
};

export default api;
