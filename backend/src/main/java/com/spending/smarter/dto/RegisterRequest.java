package com.spending.smarter.dto;

public class RegisterRequest {
    private String email;
    private String password;
    private String fullName;
    private Double monthlyIncome;
    private String currency = "USD";

    public RegisterRequest() {}

    public RegisterRequest(String email, String password, String fullName, Double monthlyIncome, String currency) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.monthlyIncome = monthlyIncome;
        this.currency = currency;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public Double getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(Double monthlyIncome) { this.monthlyIncome = monthlyIncome; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}