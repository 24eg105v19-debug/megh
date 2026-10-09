package com.spending.smarter.dto;

import com.spending.smarter.model.Budget;
import com.spending.smarter.model.Category;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BudgetResponse {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private String categoryIcon;
    private String categoryColor;
    private BigDecimal amount;
    private BigDecimal spentAmount;
    private BigDecimal remainingAmount;
    private Double progressPercentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;

    public BudgetResponse() {}

    public BudgetResponse(Long id, Long categoryId, String categoryName, String categoryIcon, String categoryColor, BigDecimal amount, BigDecimal spentAmount, BigDecimal remainingAmount, Double progressPercentage, LocalDate startDate, LocalDate endDate, LocalDateTime createdAt) {
        this.id = id;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryIcon = categoryIcon;
        this.categoryColor = categoryColor;
        this.amount = amount;
        this.spentAmount = spentAmount;
        this.remainingAmount = remainingAmount;
        this.progressPercentage = progressPercentage;
        this.startDate = startDate;
        this.endDate = endDate;
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
        private BigDecimal spentAmount;
        private BigDecimal remainingAmount;
        private Double progressPercentage;
        private LocalDate startDate;
        private LocalDate endDate;
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder categoryId(Long categoryId) { this.categoryId = categoryId; return this; }
        public Builder categoryName(String categoryName) { this.categoryName = categoryName; return this; }
        public Builder categoryIcon(String categoryIcon) { this.categoryIcon = categoryIcon; return this; }
        public Builder categoryColor(String categoryColor) { this.categoryColor = categoryColor; return this; }
        public Builder amount(BigDecimal amount) { this.amount = amount; return this; }
        public Builder spentAmount(BigDecimal spentAmount) { this.spentAmount = spentAmount; return this; }
        public Builder remainingAmount(BigDecimal remainingAmount) { this.remainingAmount = remainingAmount; return this; }
        public Builder progressPercentage(Double progressPercentage) { this.progressPercentage = progressPercentage; return this; }
        public Builder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public Builder endDate(LocalDate endDate) { this.endDate = endDate; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public BudgetResponse build() {
            return new BudgetResponse(id, categoryId, categoryName, categoryIcon, categoryColor, amount, spentAmount, remainingAmount, progressPercentage, startDate, endDate, createdAt);
        }
    }

    public static BudgetResponse from(Budget budget) {
        Category category = budget.getCategory();
        BigDecimal remaining = budget.getAmount().subtract(budget.getSpentAmount());
        Double progress = budget.getAmount().compareTo(BigDecimal.ZERO) > 0 
                ? (budget.getSpentAmount().doubleValue() / budget.getAmount().doubleValue()) * 100 
                : 0.0;
        
        return BudgetResponse.builder()
                .id(budget.getId())
                .categoryId(category.getId())
                .categoryName(category.getName())
                .categoryIcon(category.getIconName())
                .categoryColor(category.getColor())
                .amount(budget.getAmount())
                .spentAmount(budget.getSpentAmount())
                .remainingAmount(remaining)
                .progressPercentage(progress)
                .startDate(budget.getStartDate())
                .endDate(budget.getEndDate())
                .createdAt(budget.getCreatedAt())
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
    public BigDecimal getSpentAmount() { return spentAmount; }
    public void setSpentAmount(BigDecimal spentAmount) { this.spentAmount = spentAmount; }
    public BigDecimal getRemainingAmount() { return remainingAmount; }
    public void setRemainingAmount(BigDecimal remainingAmount) { this.remainingAmount = remainingAmount; }
    public Double getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(Double progressPercentage) { this.progressPercentage = progressPercentage; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}