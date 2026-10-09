# Spending Smarter — Smart Personal Expense Management

Full-stack personal expense management system with a Spring Boot API, React frontend and MySQL database.

## Live Deployment
- **Website (Frontend):** https://megh-one.vercel.app
- **API (Backend):** https://spending-smarter-backend.onrender.com/api

See [PROJECT.md](./PROJECT.md) for the full project summary and links.

## Tech Stack
| Layer | Technology |
|-------|------------|
| Frontend | React 18 + Vite (Chart.js, React Router) |
| Backend | Java 17 / Spring Boot 3.3 (Maven), Spring Security + JWT |
| Database | MySQL |
| Deployment | Vercel (frontend), Render Docker (backend), Aiven (MySQL) |

## Project Layout
```
megh/
├─ backend/                  Spring Boot (Maven) API
│  ├─ src/main/java/com/spending/smarter/
│  │  ├─ config/             JWT, Security, DataSeeder
│  │  ├─ controller/         Auth, Expenses, Incomes, Budgets, Dashboard, Categories, User
│  │  ├─ dto/                Request/response objects
│  │  ├─ exception/          Global error handling
│  │  ├─ model/              User, Expense, Income, Budget, Category
│  │  ├─ repository/         Spring Data JPA repositories
│  │  └─ service/            Business logic
│  ├─ src/main/resources/application.properties
│  └─ Dockerfile
├─ frontend/                 React + Vite SPA
│  ├─ src/                   pages, components, context, api client
│  ├─ vercel.json            /api proxy + SPA rewrites
│  └─ vite.config.js
├─ database/                 Manual SQL scripts
├─ render.yaml               Render blueprint
├─ PROJECT.md                Project summary
└─ README.md
```

## API Endpoints
| Method | Endpoint | Notes |
|--------|----------|-------|
| POST | `/api/auth/register` | User registration → token |
| POST | `/api/auth/login` | User login → token |
| GET | `/api/categories` | All categories (public) |
| GET | `/api/categories/type/{type}` | Categories by type |
| GET | `/api/expenses` | List expenses (auth) — `start`, `end` params |
| POST | `/api/expenses` | Create expense (auth) |
| PUT | `/api/expenses/{id}` | Update expense (auth) |
| DELETE | `/api/expenses/{id}` | Delete expense (auth) |
| GET | `/api/expenses/total` | Total expenses in range (auth) |
| GET | `/api/expenses/by-category` | Category breakdown (auth) |
| GET | `/api/incomes` | List incomes (auth) — `start`, `end` params |
| POST | `/api/incomes` | Create income (auth) |
| PUT | `/api/incomes/{id}` | Update income (auth) |
| DELETE | `/api/incomes/{id}` | Delete income (auth) |
| GET | `/api/budgets` | List budgets (auth) |
| GET | `/api/budgets/active` | Active budgets (auth) — `date` param |
| POST | `/api/budgets` | Create budget (auth) |
| PUT | `/api/budgets/{id}` | Update budget (auth) |
| DELETE | `/api/budgets/{id}` | Delete budget (auth) |
| GET | `/api/dashboard` | Dashboard summary (auth) |
| GET | `/api/user/profile` | Get profile (auth) |
| PUT | `/api/user/profile` | Update profile (auth) |

Requests that require auth send `Authorization: Bearer <token>`.

## How To Run (Windows)
### Prerequisites
- MySQL running on localhost:3306
- Java 17+
- Node 18+

### 1. Backend
```cmd
cd backend
mvn spring-boot:run
```
Wait until: `Started SmarterApplication ... port 8080`.
Default local DB: `spending_smarter` on `localhost:3306` (root/root), auto-created and seeded with default categories.

### 2. Frontend
```cmd
cd frontend
npm install
npm run dev
```
Open http://localhost:5173 (Vite proxies `/api` to http://localhost:8080).

## Configuration
Backend reads configuration from environment variables (with sensible local defaults):
```properties
SPRING_DATASOURCE_URL=jdbc:mysql://<host>:<port>/<db>?sslMode=REQUIRED&allowPublicKeyRetrieval=true&serverTimezone=UTC
SPRING_DATASOURCE_USERNAME=...
SPRING_DATASOURCE_PASSWORD=...
JWT_SECRET=...
JWT_EXPIRATION=86400000
APP_CORS_ALLOWED_ORIGINS=https://megh-one.vercel.app,http://localhost:5173
PORT=8080
```

## License
MIT
