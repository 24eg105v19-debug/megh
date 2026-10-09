package com.spending.smarter.dto;

public class CategoryExpenseSummary {
    private String categoryName;
    private String iconName;
    private String color;
    private java.math.BigDecimal totalAmount;
    
    public CategoryExpenseSummary() {}

    public CategoryExpenseSummary(String categoryName, String iconName, String color, java.math.BigDecimal totalAmount) {
        this.categoryName = categoryName;
        this.iconName = iconName;
        this.color = color;
        this.totalAmount = totalAmount;
    }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getIconName() { return iconName; }
    public void setIconName(String iconName) { this.iconName = iconName; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public java.math.BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(java.math.BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}