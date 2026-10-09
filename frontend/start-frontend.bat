@echo off
echo Starting BudgetBuddy Frontend...
cd /d "%~dp0"
npm install
npm run dev
pause