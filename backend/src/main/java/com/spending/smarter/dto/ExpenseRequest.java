package com.spending.smarter.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseRequest {
    private Long categoryId;
    private BigDecimal amount;
    private LocalDate expenseDate;
    private String description;
    private Boolean isRecurring = false;
    private String recurringFrequency;

    public ExpenseRequest() {}

    public ExpenseRequest(Long categoryId, BigDecimal amount, LocalDate expenseDate, String description, Boolean isRecurring, String recurringFrequency) {
        this.categoryId = categoryId;
        this.amount = amount;
        this.expenseDate = expenseDate;
        this.description = description;
        this.isRecurring = isRecurring;
        this.recurringFrequency = recurringFrequency;
    }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
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
}