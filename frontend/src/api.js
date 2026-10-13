import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_URL || '/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 60000,
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

const sleep = (ms) => new Promise((resolve) => setTimeout(resolve, ms));
const MAX_RETRIES = 8;

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const config = error.config || {};
    const status = error.response?.status;
    const method = (config.method || 'get').toLowerCase();
    const isColdStart = !error.response || status === 502 || status === 503 || status === 504 || (status && status >= 500);

    // Retry idempotent reads so the app survives the Render free-tier cold start.
    // Delays 2,4,6,8,10... capped at 10s, so a read keeps trying for ~70s.
    if (method === 'get' && isColdStart && (config.__retryCount || 0) < MAX_RETRIES) {
      config.__retryCount = (config.__retryCount || 0) + 1;
      await sleep(Math.min(2000 * config.__retryCount, 10000));
      return api(config);
    }

    if (status === 401 || status === 403) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      if (!window.location.pathname.startsWith('/login')) {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

// Fire-and-forget warm-up so a sleeping backend is awake before the user interacts.
export const warmUp = () => api.get('/categories').catch(() => {});

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
