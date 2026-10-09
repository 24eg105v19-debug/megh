package com.spending.smarter.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class IncomeRequest {
    private Long categoryId;
    private BigDecimal amount;
    private LocalDate incomeDate;
    private String description;
    private Boolean isRecurring = false;
    private String recurringFrequency;

    public IncomeRequest() {}

    public IncomeRequest(Long categoryId, BigDecimal amount, LocalDate incomeDate, String description, Boolean isRecurring, String recurringFrequency) {
        this.categoryId = categoryId;
        this.amount = amount;
        this.incomeDate = incomeDate;
        this.description = description;
        this.isRecurring = isRecurring;
        this.recurringFrequency = recurringFrequency;
    }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
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
}