package com.spending.smarter.service;

import com.spending.smarter.model.Expense;
import com.spending.smarter.model.User;
import com.spending.smarter.model.Category;
import com.spending.smarter.repository.ExpenseRepository;
import com.spending.smarter.repository.CategoryRepository;
import com.spending.smarter.dto.ExpenseRequest;
import com.spending.smarter.dto.ExpenseResponse;
import com.spending.smarter.exception.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final BudgetService budgetService;

    public ExpenseService(ExpenseRepository expenseRepository, CategoryRepository categoryRepository, BudgetService budgetService) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.budgetService = budgetService;
    }

    @Transactional
    public ExpenseResponse create(Long userId, ExpenseRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ApiException("Category not found"));
        
        if (category.getType() != Category.CategoryType.EXPENSE) {
            throw new ApiException("Invalid category type for expense");
        }

        Expense expense = Expense.builder()
                .user(User.builder().id(userId).build())
                .category(category)
                .amount(request.getAmount())
                .expenseDate(request.getExpenseDate())
                .description(request.getDescription())
                .isRecurring(request.getIsRecurring())
                .recurringFrequency(request.getRecurringFrequency() != null 
                        ? Expense.RecurringFrequency.valueOf(request.getRecurringFrequency()) 
                        : null)
                .build();

        expense = expenseRepository.save(expense);
        
        budgetService.updateBudgetSpent(userId, category.getId(), request.getExpenseDate(), request.getAmount());

        return ExpenseResponse.from(expense);
    }

    public List<ExpenseResponse> getByDateRange(Long userId, LocalDate start, LocalDate end) {
        User user = User.builder().id(userId).build();
        return expenseRepository.findByUserAndExpenseDateBetween(user, start, end).stream()
                .map(ExpenseResponse::from)
                .collect(Collectors.toList());
    }

    public List<ExpenseResponse> getByDate(Long userId, LocalDate date) {
        User user = User.builder().id(userId).build();
        return expenseRepository.findByUserAndExpenseDate(user, date).stream()
                .map(ExpenseResponse::from)
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalExpenses(Long userId, LocalDate start, LocalDate end) {
        User user = User.builder().id(userId).build();
        BigDecimal total = expenseRepository.getTotalExpensesByUserAndDateRange(user, start, end);
        return total != null ? total : BigDecimal.ZERO;
    }

    public List<com.spending.smarter.dto.CategoryExpenseSummary> getExpensesByCategory(Long userId, LocalDate start, LocalDate end) {
        User user = User.builder().id(userId).build();
        return expenseRepository.getExpensesByCategory(user, start, end);
    }

    @Transactional
    public ExpenseResponse update(Long userId, Long expenseId, ExpenseRequest request) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ApiException("Expense not found"));
        
        if (!expense.getUser().getId().equals(userId)) {
            throw new ApiException("Unauthorized");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ApiException("Category not found"));

        budgetService.updateBudgetSpent(userId, expense.getCategory().getId(), expense.getExpenseDate(), expense.getAmount().negate());
        
        expense.setCategory(category);
        expense.setAmount(request.getAmount());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setDescription(request.getDescription());
        expense.setIsRecurring(request.getIsRecurring());
        expense.setRecurringFrequency(request.getRecurringFrequency() != null 
                ? Expense.RecurringFrequency.valueOf(request.getRecurringFrequency()) 
                : null);
        expense.setUpdatedAt(java.time.LocalDateTime.now());

        expense = expenseRepository.save(expense);
        
        budgetService.updateBudgetSpent(userId, category.getId(), request.getExpenseDate(), request.getAmount());

        return ExpenseResponse.from(expense);
    }

    @Transactional
    public void delete(Long userId, Long expenseId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ApiException("Expense not found"));
        
        if (!expense.getUser().getId().equals(userId)) {
            throw new ApiException("Unauthorized");
        }

        budgetService.updateBudgetSpent(userId, expense.getCategory().getId(), expense.getExpenseDate(), expense.getAmount().negate());
        
        expenseRepository.delete(expense);
    }

    public List<Expense> getRecurringExpenses(Long userId) {
        User user = User.builder().id(userId).build();
        return expenseRepository.findByUserAndIsRecurringTrue(user);
    }
}