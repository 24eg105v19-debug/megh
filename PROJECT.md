# Spending Smarter — Project Summary

## About Project
**Spending Smarter** is a full-stack personal finance and expense management web application.

Users can:
- Register / log in securely (JWT authentication)
- Track expenses with categories, dates and descriptions
- Set monthly category budgets and track progress against spending
- View a dashboard with charts (expenses by category, budget progress) and savings rate

### Tech Stack
| Layer | Technology |
|-------|------------|
| Frontend | React 18 + Vite + Chart.js + React Router |
| Backend | Java 17 / Spring Boot 3.3 (Maven), Spring Security + JWT, Spring Data JPA |
| Database | MySQL (hosted on Aiven, SSL-secured) |
| Deployment | Vercel (frontend), Render Docker (backend), Aiven (MySQL) |

## Frontend (GitHub Link)
https://github.com/24eg105v19-debug/megh/tree/main/frontend

## Backend (GitHub Link)
https://github.com/24eg105v19-debug/megh/tree/main/backend

## Final Project Deployment
- **Live Website (Frontend):** https://megh-one.vercel.app
- **Live API (Backend):** https://spending-smarter-backend.onrender.com/api
- **Database:** Aiven MySQL (`defaultdb`), SSL-secured credentials stored as Render environment variables
- **Source Repository:** https://github.com/24eg105v19-debug/megh

### Deployment Details
- Frontend is hosted on **Vercel**; `/api/*` requests are proxied to the Render backend via `vercel.json`.
- Backend is built as a **Docker** image (`backend/Dockerfile`) and deployed on **Render** (free web service).
- Database connection and secrets are supplied through Render environment variables
  (`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `JWT_SECRET`, `APP_CORS_ALLOWED_ORIGINS`).
- CORS on the backend is restricted to the deployed frontend origin.
- Schema/tables are auto-created by Hibernate (`ddl-auto=update`) and default categories are seeded on startup.

## API Endpoints
| Method | Endpoint | Notes |
|--------|----------|-------|
| POST | `/api/auth/register` | Register → JWT |
| POST | `/api/auth/login` | Login → JWT |
| GET | `/api/categories` | All categories (public) |
| GET | `/api/categories/type/{type}` | Categories by type (EXPENSE / INCOME) |
| GET | `/api/expenses?start&end` | List expenses in date range |
| POST | `/api/expenses` | Create expense |
| PUT | `/api/expenses/{id}` | Update expense |
| DELETE | `/api/expenses/{id}` | Delete expense |
| GET | `/api/expenses/total?start&end` | Total expenses |
| GET | `/api/expenses/by-category?start&end` | Category breakdown |
| GET | `/api/incomes?start&end` | List incomes in date range |
| POST | `/api/incomes` | Create income |
| PUT | `/api/incomes/{id}` | Update income |
| DELETE | `/api/incomes/{id}` | Delete income |
| GET | `/api/budgets` | List budgets |
| GET | `/api/budgets/active?date` | Active budgets for a date |
| POST | `/api/budgets` | Create budget |
| PUT | `/api/budgets/{id}` | Update budget |
| DELETE | `/api/budgets/{id}` | Delete budget |
| GET | `/api/dashboard` | Dashboard summary |
| GET | `/api/user/profile` | Get profile |
| PUT | `/api/user/profile` | Update profile (fullName, monthlyIncome, currency) |

Authenticated requests send `Authorization: Bearer <token>`.

## Local Development
Backend:
```bash
cd backend
mvn spring-boot:run
```
Frontend:
```bash
cd frontend
npm install
npm run dev
```
Open http://localhost:5173 (the Vite dev server proxies `/api` to http://localhost:8080).
