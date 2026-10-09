package com.spending.smarter.dto;

import com.spending.smarter.model.Category;
import com.spending.smarter.model.Expense;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ExpenseResponse {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private String categoryIcon;
    private String categoryColor;
    private BigDecimal amount;
    private LocalDate expenseDate;
    private String description;
    private Boolean isRecurring;
    private String recurringFrequency;
    private LocalDateTime createdAt;

    public ExpenseResponse() {}

    public ExpenseResponse(Long id, Long categoryId, String categoryName, String categoryIcon, String categoryColor, BigDecimal amount, LocalDate expenseDate, String description, Boolean isRecurring, String recurringFrequency, LocalDateTime createdAt) {
        this.id = id;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryIcon = categoryIcon;
        this.categoryColor = categoryColor;
        this.amount = amount;
        this.expenseDate = expenseDate;
        this.description = description;
        this.isRecurring = isRecurring;
        this.recurringFrequency = recurringFrequency;
        this.createdAt = createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long categoryId;
        private String categoryName;
        private String categoryIcon;
        private String categoryColor;
        private BigDecimal amount;
        private LocalDate expenseDate;
        private String description;
        private Boolean isRecurring;
        private String recurringFrequency;
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder categoryId(Long categoryId) { this.categoryId = categoryId; return this; }
        public Builder categoryName(String categoryName) { this.categoryName = categoryName; return this; }
        public Builder categoryIcon(String categoryIcon) { this.categoryIcon = categoryIcon; return this; }
        public Builder categoryColor(String categoryColor) { this.categoryColor = categoryColor; return this; }
        public Builder amount(BigDecimal amount) { this.amount = amount; return this; }
        public Builder expenseDate(LocalDate expenseDate) { this.expenseDate = expenseDate; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder isRecurring(Boolean isRecurring) { this.isRecurring = isRecurring; return this; }
        public Builder recurringFrequency(String recurringFrequency) { this.recurringFrequency = recurringFrequency; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public ExpenseResponse build() {
            return new ExpenseResponse(id, categoryId, categoryName, categoryIcon, categoryColor, amount, expenseDate, description, isRecurring, recurringFrequency, createdAt);
        }
    }

    public static ExpenseResponse from(Expense expense) {
        Category category = expense.getCategory();
        return ExpenseResponse.builder()
                .id(expense.getId())
                .categoryId(category.getId())
                .categoryName(category.getName())
                .categoryIcon(category.getIconName())
                .categoryColor(category.getColor())
                .amount(expense.getAmount())
                .expenseDate(expense.getExpenseDate())
                .description(expense.getDescription())
                .isRecurring(expense.getIsRecurring())
                .recurringFrequency(expense.getRecurringFrequency() != null ? expense.getRecurringFrequency().name() : null)
                .createdAt(expense.getCreatedAt())
                .build();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getCategoryIcon() { return categoryIcon; }
    public void setCategoryIcon(String categoryIcon) { this.categoryIcon = categoryIcon; }
    public String getCategoryColor() { return categoryColor; }
    public void setCategoryColor(String categoryColor) { this.categoryColor = categoryColor; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDate getExpenseDate() { return expenseDate; }
    public void setExpenseDate(LocalDate expenseDate) { this.expenseDate = expenseDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getIsRecurring() { return isRecurring; }
    public void setIsRecurring(Boolean isRecurring) { this.isRecurring = isRecurring; }
    public String getRecurringFrequency() { return recurringFrequency; }
    public void setRecurringFrequency(String recurringFrequency) { this.recurringFrequency = recurringFrequency; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}