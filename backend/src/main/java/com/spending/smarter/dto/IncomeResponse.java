package com.spending.smarter.dto;

import com.spending.smarter.model.Category;
import com.spending.smarter.model.Income;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class IncomeResponse {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private String categoryIcon;
    private String categoryColor;
    private BigDecimal amount;
    private LocalDate incomeDate;
    private String description;
    private Boolean isRecurring;
    private String recurringFrequency;
    private LocalDateTime createdAt;

    public IncomeResponse() {}

    public IncomeResponse(Long id, Long categoryId, String categoryName, String categoryIcon, String categoryColor, BigDecimal amount, LocalDate incomeDate, String description, Boolean isRecurring, String recurringFrequency, LocalDateTime createdAt) {
        this.id = id;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryIcon = categoryIcon;
        this.categoryColor = categoryColor;
        this.amount = amount;
        this.incomeDate = incomeDate;
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
        private LocalDate incomeDate;
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
        public Builder incomeDate(LocalDate incomeDate) { this.incomeDate = incomeDate; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder isRecurring(Boolean isRecurring) { this.isRecurring = isRecurring; return this; }
        public Builder recurringFrequency(String recurringFrequency) { this.recurringFrequency = recurringFrequency; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public IncomeResponse build() {
            return new IncomeResponse(id, categoryId, categoryName, categoryIcon, categoryColor, amount, incomeDate, description, isRecurring, recurringFrequency, createdAt);
        }
    }

    public static IncomeResponse from(Income income) {
        Category category = income.getCategory();
        return IncomeResponse.builder()
                .id(income.getId())
                .categoryId(category != null ? category.getId() : null)
                .categoryName(category != null ? category.getName() : null)
                .categoryIcon(category != null ? category.getIconName() : null)
                .categoryColor(category != null ? category.getColor() : null)
                .amount(income.getAmount())
                .incomeDate(income.getIncomeDate())
                .description(income.getDescription())
                .isRecurring(income.getIsRecurring())
                .recurringFrequency(income.getRecurringFrequency() != null ? income.getRecurringFrequency().name() : null)
                .createdAt(income.getCreatedAt())
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
    public LocalDate getIncomeDate() { return incomeDate; }
    public void setIncomeDate(LocalDate incomeDate) { this.incomeDate = incomeDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getIsRecurring() { return isRecurring; }
    public void setIsRecurring(Boolean isRecurring) { this.isRecurring = isRecurring; }
    public String getRecurringFrequency() { return recurringFrequency; }
    public void setRecurringFrequency(String recurringFrequency) { this.recurringFrequency = recurringFrequency; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}