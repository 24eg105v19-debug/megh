package com.spending.smarter.service;

import com.spending.smarter.model.User;
import com.spending.smarter.repository.ExpenseRepository;
import com.spending.smarter.repository.IncomeRepository;
import com.spending.smarter.repository.UserRepository;
import com.spending.smarter.dto.DashboardSummary;
import com.spending.smarter.dto.CategoryExpenseSummary;
import com.spending.smarter.dto.BudgetResponse;
import com.spending.smarter.dto.ExpenseResponse;
import com.spending.smarter.dto.IncomeResponse;
import com.spending.smarter.dto.MonthlyTotal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;
    private final BudgetService budgetService;
    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    public DashboardSummary getDashboardSummary(Long userId) {
        User user = User.builder().id(userId).build();
        
        YearMonth currentMonth = YearMonth.now();
        LocalDate monthStart = currentMonth.atDay(1);
        LocalDate monthEnd = currentMonth.atEndOfMonth();
        
        BigDecimal totalIncome = incomeRepository.getTotalIncomeByUserAndDateRange(user, LocalDate.of(2000, 1, 1), LocalDate.now());
        BigDecimal totalExpenses = expenseRepository.getTotalExpensesByUserAndDateRange(user, LocalDate.of(2000, 1, 1), LocalDate.now());
        
        totalIncome = totalIncome != null ? totalIncome : BigDecimal.ZERO;
        totalExpenses = totalExpenses != null ? totalExpenses : BigDecimal.ZERO;
        
        BigDecimal monthlyIncome = incomeRepository.getTotalIncomeByUserAndDateRange(user, monthStart, monthEnd);
        BigDecimal monthlyExpenses = expenseRepository.getTotalExpensesByUserAndDateRange(user, monthStart, monthEnd);
        
        monthlyIncome = monthlyIncome != null ? monthlyIncome : BigDecimal.ZERO;
        monthlyExpenses = monthlyExpenses != null ? monthlyExpenses : BigDecimal.ZERO;

        // Prefer the user's declared monthly income (set from the dashboard) when present.
        BigDecimal declaredIncome = userRepository.findById(userId)
                .map(User::getMonthlyIncome)
                .filter(v -> v != null && v > 0)
                .map(BigDecimal::valueOf)
                .orElse(BigDecimal.ZERO);
        if (declaredIncome.compareTo(BigDecimal.ZERO) > 0) {
            monthlyIncome = declaredIncome;
        }
        
        List<BudgetResponse> activeBudgets = budgetService.getActiveBudgets(userId, LocalDate.now());
        BigDecimal monthlyBudget = activeBudgets.stream()
                .map(BudgetResponse::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        List<CategoryExpenseSummary> topExpenseCategories = expenseRepository.getExpensesByCategory(user, monthStart, monthEnd);
        
        List<ExpenseResponse> recentExpenses = expenseService.getByDateRange(userId, monthStart.minusMonths(1), monthEnd);
        List<IncomeResponse> recentIncomes = incomeService.getByDateRange(userId, monthStart.minusMonths(1), monthEnd);

        // Last 6 calendar months (oldest -> newest) for the spending trend chart.
        List<MonthlyTotal> monthlyTrend = new ArrayList<>();
        YearMonth trendStart = currentMonth.minusMonths(5);
        for (int i = 0; i < 6; i++) {
            YearMonth ym = trendStart.plusMonths(i);
            BigDecimal inc = incomeRepository.getTotalIncomeByUserAndDateRange(user, ym.atDay(1), ym.atEndOfMonth());
            BigDecimal exp = expenseRepository.getTotalExpensesByUserAndDateRange(user, ym.atDay(1), ym.atEndOfMonth());
            monthlyTrend.add(new MonthlyTotal(
                    ym.toString(),
                    exp != null ? exp : BigDecimal.ZERO,
                    inc != null ? inc : BigDecimal.ZERO));
        }

        return DashboardSummary.builder()
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .netSavings(totalIncome.subtract(totalExpenses))
                .monthlyIncome(monthlyIncome)
                .monthlyExpenses(monthlyExpenses)
                .monthlyBudget(monthlyBudget)
                .topExpenseCategories(topExpenseCategories)
                .activeBudgets(activeBudgets)
                .recentExpenses(recentExpenses.stream().limit(10).collect(java.util.stream.Collectors.toList()))
                .recentIncomes(recentIncomes.stream().limit(10).collect(java.util.stream.Collectors.toList()))
                .monthlyTrend(monthlyTrend)
                .build();
    }
}