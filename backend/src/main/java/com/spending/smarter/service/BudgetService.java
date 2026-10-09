package com.spending.smarter.service;

import com.spending.smarter.model.Budget;
import com.spending.smarter.model.User;
import com.spending.smarter.model.Category;
import com.spending.smarter.repository.BudgetRepository;
import com.spending.smarter.repository.CategoryRepository;
import com.spending.smarter.repository.ExpenseRepository;
import com.spending.smarter.dto.BudgetRequest;
import com.spending.smarter.dto.BudgetResponse;
import com.spending.smarter.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;

    @Transactional
    public BudgetResponse create(Long userId, BudgetRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ApiException("Category not found"));

        if (category.getType() != Category.CategoryType.EXPENSE) {
            throw new ApiException("Budgets can only be set for expense categories");
        }

        Optional<Budget> existing = budgetRepository.findByUserAndCategoryAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                User.builder().id(userId).build(), category, request.getStartDate(), request.getEndDate());
        
        if (existing.isPresent()) {
            throw new ApiException("Budget already exists for this category and date range");
        }

        BigDecimal spent = expenseRepository.getTotalExpensesByUserAndDateRange(
                User.builder().id(userId).build(), request.getStartDate(), request.getEndDate());
        spent = spent != null ? spent : BigDecimal.ZERO;

        Budget budget = Budget.builder()
                .user(User.builder().id(userId).build())
                .category(category)
                .amount(request.getAmount())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .spentAmount(spent)
                .build();

        budget = budgetRepository.save(budget);
        return BudgetResponse.from(budget);
    }

    public List<BudgetResponse> getActiveBudgets(Long userId, LocalDate date) {
        User user = User.builder().id(userId).build();
        return budgetRepository.findActiveBudgetsByUserAndDate(user, date).stream()
                .map(BudgetResponse::from)
                .collect(Collectors.toList());
    }

    public List<BudgetResponse> getAllBudgets(Long userId) {
        User user = User.builder().id(userId).build();
        return budgetRepository.findByUser(user).stream()
                .map(BudgetResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public BudgetResponse update(Long userId, Long budgetId, BudgetRequest request) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ApiException("Budget not found"));
        
        if (!budget.getUser().getId().equals(userId)) {
            throw new ApiException("Unauthorized");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ApiException("Category not found"));

        budget.setCategory(category);
        budget.setAmount(request.getAmount());
        budget.setStartDate(request.getStartDate());
        budget.setEndDate(request.getEndDate());
        budget.setUpdatedAt(java.time.LocalDateTime.now());

        budget = budgetRepository.save(budget);
        return BudgetResponse.from(budget);
    }

    @Transactional
    public void delete(Long userId, Long budgetId) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ApiException("Budget not found"));
        
        if (!budget.getUser().getId().equals(userId)) {
            throw new ApiException("Unauthorized");
        }

        budgetRepository.delete(budget);
    }

    @Transactional
    public void updateBudgetSpent(Long userId, Long categoryId, LocalDate date, BigDecimal amount) {
        User user = User.builder().id(userId).build();
        Category category = Category.builder().id(categoryId).build();
        
        List<Budget> budgets = budgetRepository.findActiveBudgetsByUserAndDate(user, date);
        for (Budget budget : budgets) {
            if (budget.getCategory().getId().equals(categoryId)) {
                budget.setSpentAmount(budget.getSpentAmount().add(amount));
                budget.setUpdatedAt(java.time.LocalDateTime.now());
                budgetRepository.save(budget);
            }
        }
    }
}