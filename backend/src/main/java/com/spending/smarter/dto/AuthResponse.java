package com.spending.smarter.dto;

public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private String fullName;
    private Double monthlyIncome;
    private String currency;

    public AuthResponse() {}

    public AuthResponse(String token, String type, Long id, String email, String fullName, Double monthlyIncome, String currency) {
        this.token = token;
        this.type = type;
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.monthlyIncome = monthlyIncome;
        this.currency = currency;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String token;
        private String type = "Bearer";
        private Long id;
        private String email;
        private String fullName;
        private Double monthlyIncome;
        private String currency;

        public Builder token(String token) { this.token = token; return this; }
        public Builder type(String type) { this.type = type; return this; }
        public Builder id(Long id) { this.id = id; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder fullName(String fullName) { this.fullName = fullName; return this; }
        public Builder monthlyIncome(Double monthlyIncome) { this.monthlyIncome = monthlyIncome; return this; }
        public Builder currency(String currency) { this.currency = currency; return this; }

        public AuthResponse build() {
            return new AuthResponse(token, type, id, email, fullName, monthlyIncome, currency);
        }
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public Double getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(Double monthlyIncome) { this.monthlyIncome = monthlyIncome; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}