package com.spending.smarter.dto;

import java.math.BigDecimal;

public class MonthlyTotal {
    private String month;
    private BigDecimal expenses;
    private BigDecimal income;

    public MonthlyTotal() {}

    public MonthlyTotal(String month, BigDecimal expenses, BigDecimal income) {
        this.month = month;
        this.expenses = expenses;
        this.income = income;
    }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }
    public BigDecimal getExpenses() { return expenses; }
    public void setExpenses(BigDecimal expenses) { this.expenses = expenses; }
    public BigDecimal getIncome() { return income; }
    public void setIncome(BigDecimal income) { this.income = income; }
}
