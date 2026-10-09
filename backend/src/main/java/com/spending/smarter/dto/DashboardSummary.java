package com.spending.smarter.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardSummary {
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netSavings;
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyExpenses;
    private BigDecimal monthlyBudget;
    private List<CategoryExpenseSummary> topExpenseCategories;
    private List<BudgetResponse> activeBudgets;
    private List<ExpenseResponse> recentExpenses;
    private List<IncomeResponse> recentIncomes;
}