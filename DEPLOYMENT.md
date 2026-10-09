# Deployment Guide — Aiven (MySQL) + Render (backend) + Vercel (frontend)

Stack: React (Vite) on Vercel, Spring Boot (Docker) on Render, MySQL on Aiven.

---

## 0. Prerequisites
- GitHub account (have) + Git installed (see Step 0.1)
- Aiven, Render, Vercel accounts (all free, sign in with GitHub)

### 0.1 Install Git (needed to push to GitHub)
Download and install: https://git-scm.com/download/win
After install, reopen the terminal and verify:
```
git --version
```

### 0.2 Push this project to GitHub
From `C:\Users\Srishanth\OneDrive\Pictures\Desktop\megh`:
```
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/<your-username>/megh.git
git push -u origin main
```

---

## 1. Online MySQL — Aiven
1. Go to https://aiven.io → Sign up (free).
2. Create a service → **MySQL** → choose the free plan → pick a region close to you.
3. Wait until the service is **Running**, then open it → **Connection information**.
4. Note these values:
   - Host, Port, Database name, User, Password
   - Download the CA certificate (optional; driver can use `useSSL=true`).

Your JDBC URL will be:
```
jdbc:mysql://<HOST>:<PORT>/<DB>?useSSL=true&requireSSL=true&allowPublicKeyRetrieval=true&serverTimezone=UTC
```
> Aiven requires SSL. We already enable `allowPublicKeyRetrieval` + `serverTimezone`.

---

## 2. Backend — Render (Docker)
Repo already contains `render.yaml` and `backend/Dockerfile`.

1. Go to https://render.com → New → **Blueprint** → connect your `megh` repo.
   - Render detects `render.yaml` and creates the `spending-smarter-backend` web service.
   - (Or: New → **Web Service** → Docker → Dockerfile path `backend/Dockerfile`, context `backend`.)
2. Set the environment variables when prompted:

| Key | Value |
|-----|-------|
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://<HOST>:<PORT>/<DB>?useSSL=true&requireSSL=true&allowPublicKeyRetrieval=true&serverTimezone=UTC` |
| `SPRING_DATASOURCE_USERNAME` | your Aiven user |
| `SPRING_DATASOURCE_PASSWORD` | your Aiven password |
| `JWT_SECRET` | a long random string (32+ chars) |
| `JWT_EXPIRATION` | `86400000` |
| `APP_CORS_ALLOWED_ORIGINS` | (set after Step 3, e.g. `https://megh.vercel.app`) |

3. Deploy. Render injects `PORT` automatically — the app reads `${PORT:8080}`.
4. When live, your API is at: `https://<service-name>.onrender.com/api`
   - Test: `https://<service-name>.onrender.com/api/categories`

> Free tier sleeps after ~15 min idle; first request may take ~30–60s.

---

## 3. Frontend — Vercel
1. Go to https://vercel.com → Add New → **Project** → import the same `megh` repo.
2. Settings:
   - **Root Directory**: `frontend`
   - Framework preset: Vite (auto-detected)
   - Build command: `npm run build`
   - Output directory: `dist`
3. Environment variables:

| Key | Value |
|-----|-------|
| `VITE_API_URL` | `https://<service-name>.onrender.com/api` |

4. Deploy. You get a public URL, e.g. `https://megh.vercel.app`.

---

## 4. Wire CORS (important)
After Vercel gives you the URL, go back to Render → backend service → Environment and set:
```
APP_CORS_ALLOWED_ORIGINS=https://megh.vercel.app
```
(Add multiple with commas if needed.) Redeploy the backend.

---

## 5. Verify
1. Open the Vercel URL.
2. Register a new account or log in with the seeded demo user (`demo@spending-smarter.com` / `demo123` if the seeder ran).
3. Confirm data loads (no CORS/network errors in the browser console).

---

## Local development (unchanged)
- Backend: `cd backend` then `mvn spring-boot:run` (uses local MySQL defaults).
- Frontend: `cd frontend` then `npm install` and `npm run dev` (proxies `/api` → `localhost:8080`).
