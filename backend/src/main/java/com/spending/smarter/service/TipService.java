package com.spending.smarter.service;

import com.spending.smarter.dto.BudgetResponse;
import com.spending.smarter.dto.CategoryExpenseSummary;
import com.spending.smarter.dto.TipResponse;
import com.spending.smarter.exception.ApiException;
import com.spending.smarter.model.Tip;
import com.spending.smarter.model.User;
import com.spending.smarter.repository.ExpenseRepository;
import com.spending.smarter.repository.IncomeRepository;
import com.spending.smarter.repository.TipRepository;
import com.spending.smarter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TipService {
    private final TipRepository tipRepository;
    private final BudgetService budgetService;
    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    public List<TipResponse> getAll(Long userId) {
        return tipRepository.findByUserOrderByCreatedAtDesc(User.builder().id(userId).build()).stream()
                .map(TipResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<TipResponse> generate(Long userId) {
        tipRepository.deleteByUser(User.builder().id(userId).build());
        tipRepository.saveAll(buildTips(userId));
        return getAll(userId);
    }

    @Transactional
    public TipResponse markRead(Long userId, Long tipId) {
        Tip tip = tipRepository.findById(tipId)
                .orElseThrow(() -> new ApiException("Tip not found"));
        if (!tip.getUser().getId().equals(userId)) {
            throw new ApiException("Unauthorized");
        }
        tip.setIsRead(true);
        return TipResponse.from(tipRepository.save(tip));
    }

    private List<Tip> buildTips(Long userId) {
        List<Tip> tips = new ArrayList<>();
        User user = User.builder().id(userId).build();
        YearMonth currentMonth = YearMonth.now();
        LocalDate monthStart = currentMonth.atDay(1);
        LocalDate monthEnd = currentMonth.atEndOfMonth();

        List<BudgetResponse> activeBudgets = budgetService.getActiveBudgets(userId, LocalDate.now());
        if (activeBudgets.isEmpty()) {
            tips.add(makeTip(userId, "Budgets", "MEDIUM",
                    "You haven't set any budgets yet. Create a budget for each expense category to keep your spending in check."));
        } else {
            for (BudgetResponse budget : activeBudgets) {
                double pct = budget.getProgressPercentage() != null ? budget.getProgressPercentage() : 0;
                if (pct > 100) {
                    tips.add(makeTip(userId, budget.getCategoryName(), "HIGH",
                            "You've exceeded your " + budget.getCategoryName() + " budget. Spent " + money(budget.getSpentAmount())
                                    + " of " + money(budget.getAmount()) + "."));
                } else if (pct >= 80) {
                    tips.add(makeTip(userId, budget.getCategoryName(), "MEDIUM",
                            "You've used " + String.format("%.1f", pct) + "% of your " + budget.getCategoryName()
                                    + " budget. Slow down to stay within " + money(budget.getAmount()) + "."));
                }
            }
        }

        List<CategoryExpenseSummary> categories = expenseRepository.getExpensesByCategory(user, monthStart, monthEnd);
        if (!categories.isEmpty()) {
            CategoryExpenseSummary top = categories.get(0);
            tips.add(makeTip(userId, top.getCategoryName(), "MEDIUM",
                    "Your biggest spending category this month is " + top.getCategoryName() + " ("
                            + money(top.getTotalAmount()) + "). Look for small ways to trim it."));
        }

        BigDecimal monthlyExpenses = nz(expenseRepository.getTotalExpensesByUserAndDateRange(user, monthStart, monthEnd));
        BigDecimal declared = userRepository.findById(userId)
                .map(User::getMonthlyIncome)
                .filter(v -> v != null && v > 0)
                .map(BigDecimal::valueOf)
                .orElse(BigDecimal.ZERO);
        BigDecimal monthlyIncome = declared.compareTo(BigDecimal.ZERO) > 0
                ? declared
                : nz(incomeRepository.getTotalIncomeByUserAndDateRange(user, monthStart, monthEnd));

        if (monthlyIncome.compareTo(BigDecimal.ZERO) > 0 && monthlyExpenses.compareTo(monthlyIncome) > 0) {
            tips.add(makeTip(userId, "Savings", "HIGH",
                    "You're spending more than your income this month (" + money(monthlyExpenses) + " spent vs "
                            + money(monthlyIncome) + " earned). Cut non-essential spending to get back on track."));
        } else if (monthlyIncome.compareTo(BigDecimal.ZERO) > 0 && monthlyExpenses.compareTo(BigDecimal.ZERO) > 0) {
            double rate = monthlyIncome.subtract(monthlyExpenses).doubleValue() / monthlyIncome.doubleValue() * 100;
            if (rate >= 20) {
                tips.add(makeTip(userId, "Savings", "LOW",
                        "Great job! You saved " + String.format("%.1f", rate) + "% of your income this month."));
            } else {
                tips.add(makeTip(userId, "Savings", "LOW",
                        "You saved " + String.format("%.1f", rate) + "% of your income this month. Try to push your savings rate past 20%."));
            }
        }

        if (monthlyExpenses.compareTo(BigDecimal.ZERO) == 0) {
            tips.add(makeTip(userId, "Tracking", "LOW",
                    "No expenses recorded this month yet. Keep tracking every purchase to get accurate insights."));
        }

        if (tips.isEmpty()) {
            tips.add(makeTip(userId, "Getting Started", "LOW",
                    "Add a few expenses and set a budget to start receiving personalized savings tips."));
        }

        return tips;
    }

    private Tip makeTip(Long userId, String category, String priorityLevel, String text) {
        return Tip.builder()
                .user(User.builder().id(userId).build())
                .category(category)
                .priorityLevel(priorityLevel)
                .tip(text)
                .isRead(false)
                .build();
    }

    private BigDecimal nz(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    private String money(BigDecimal value) {
        return "$" + nz(value).setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}