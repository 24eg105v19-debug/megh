# BudgetBuddy — Smart Personal Expense Management

Full-stack personal expense management system built with the same architecture as the Restaurant Reservation System (tt1).

## Tech Stack

| Layer     | Technology                       |
|-----------|----------------------------------|
| Frontend  | React 18 + Vite (http://localhost:5173) |
| Backend   | Java 17 / Spring Boot 3.2 (http://localhost:8080/api) |
| Database  | MySQL (`budgetbuddy_db`)         |
| Auth      | JWT (Bearer tokens)              |

## Project Layout

```
megh/
├─ backend/                  Spring Boot (Maven) API
│  ├─ src/main/java/com/budgetbuddy/expense/
│  │  ├─ config/             JWT, Security, DataSeeder
│  │  ├─ controller/         REST controllers (Auth, Expenses, Budgets, Analytics, Categories)
│  │  ├─ dto/                Request/response objects
│  │  ├─ exception/          Global error handling
│  │  ├─ model/              User, Expense, Budget, Category
│  │  ├─ repository/         Spring Data JPA repositories
│  │  └─ service/            Auth, Expense, Budget, AiInsight services
│  └─ src/main/resources/application.properties
├─ frontend/                 React + Vite SPA
│  ├─ src/
│  │  ├─ components/         Layout, shared components
│  │  ├─ pages/              Login, Register, Dashboard, Expenses, Budgets, Analytics, Settings
│  │  ├─ context/            AuthContext
│  │  ├─ services/           API client (axios)
│  │  ├─ utils/              Constants, helpers
│  │  ├─ App.jsx             Routes + protected routes
│  │  └─ main.jsx            Entry point
│  └─ index.html
├─ database/budgetbuddy_db.sql   Manual schema + seed script
├─ start-backend.bat         Double-click to run the API
├─ start-frontend.bat        Double-click to run the frontend
└─ README.md
```

## API Endpoints

| Method | Endpoint                                 | Notes |
|--------|------------------------------------------|-------|
| POST   | `/api/auth/register`                     | User registration → token |
| POST   | `/api/auth/login`                        | User login → token |
| GET    | `/api/categories`                        | All expense categories (public) |
| GET    | `/api/expenses`                          | List expenses (auth) — supports `start`, `end` query params |
| GET    | `/api/expenses/month/{year}/{month}`     | Expenses for a month (auth) |
| GET    | `/api/expenses/total`                    | Total expenses in date range (auth) |
| GET    | `/api/expenses/categories`               | Category breakdown in date range (auth) |
| POST   | `/api/expenses`                          | Create expense (auth) |
| PUT    | `/api/expenses/{id}`                     | Update expense (auth) |
| DELETE | `/api/expenses/{id}`                     | Delete expense (auth) |
| GET    | `/api/budgets`                           | List budgets (auth) — supports `year`, `month` |
| POST   | `/api/budgets`                           | Create budget (auth) |
| PUT    | `/api/budgets/{id}`                      | Update budget (auth) |
| DELETE | `/api/budgets/{id}`                      | Delete budget (auth) |
| GET    | `/api/analytics/monthly/{year}/{month}`  | Monthly summary + AI insights (auth) |
| GET    | `/api/analytics/monthly/current`         | Current month summary (auth) |

Requests that require auth send `Authorization: Bearer <token>`.

## Features

- **User Authentication** — JWT-based register/login with secure password hashing (BCrypt)
- **Expense Tracking** — Add, edit, delete expenses with categories, dates, descriptions, recurring support
- **Budget Management** — Set monthly budgets per category with alert thresholds (default 80%)
- **Visual Analytics** — Interactive charts (Chart.js): category doughnut, income/expense/savings trends, savings rate
- **AI-Powered Insights** — Personalized savings tips, budget warnings, financial health scoring
- **Recurring Expenses** — Support for weekly, biweekly, monthly, quarterly, yearly recurring transactions
- **Multi-currency** — USD, EUR, GBP, INR, JPY, CAD, AUD with formatting
- **Responsive UI** — Mobile-first design with collapsible sidebar, clean modern interface

## How To Run (Windows)

### Prerequisites
- MySQL running on localhost:3306
- Java 17+
- Node 18+

### 1. Database — Automatic
The backend connects to `root`/`root` on `localhost:3306`, auto-creates `budgetbuddy_db`, builds tables and seeds:
- Default demo user: **demo@budgetbuddy.com** / **demo123**
> Change the password in `backend/src/main/resources/application.properties` if your MySQL `root` uses something else.
> A manual script is also at `database/budgetbuddy_db.sql`.

### 2. Backend — Double-click `start-backend.bat` (or):
```cmd
cd backend
mvn package -DskipTests
java -jar target\expense-management-1.0.0.jar
```
Wait until: `Started ExpenseManagementApplication ... port 8080`.

### 3. Frontend — Double-click `start-frontend.bat` (or):
```cmd
cd frontend
npm install
npm run dev
```
Open **http://localhost:5173**.

## Demo Credentials
- **Email:** demo@budgetbuddy.com
- **Password:** demo123

## Development

### Backend Structure
- **Models:** User, Expense, Budget, Category (enum)
- **Repositories:** Spring Data JPA with custom queries for analytics
- **Services:** Business logic separated from controllers
- **Security:** JWT filter, BCrypt encoding, stateless sessions
- **Validation:** Bean Validation on DTOs + global exception handler

### Frontend Structure
- **Pages:** Dashboard, Expenses, Budgets, Analytics, Settings, Login, Register
- **State:** React Context for auth, local state for forms
- **Charts:** react-chartjs-2 (Doughnut, Bar, Line)
- **Routing:** React Router v6 with protected/public routes
- **Styling:** Pure CSS with CSS variables (supports dark mode)

## Configuration

### Backend (`application.properties`)
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/budgetbuddy_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root
jwt.secret=<base64-encoded-secret>
jwt.expiration=86400000
server.port=8080
```

### Frontend (Vite Proxy)
`vite.config.js` proxies `/api` to `http://localhost:8080` for seamless dev experience.

## License
MIT