package com.spending.smarter.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BudgetRequest {
    private Long categoryId;
    private BigDecimal amount;
    private LocalDate startDate;
    private LocalDate endDate;

    public BudgetRequest() {}

    public BudgetRequest(Long categoryId, BigDecimal amount, LocalDate startDate, LocalDate endDate) {
        this.categoryId = categoryId;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}