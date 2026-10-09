package com.spending.smarter.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "icon_name")
    private String iconName;

    @Column(name = "color", length = 7)
    private String color;

    @Column(name = "is_default")
    private Boolean isDefault = false;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryType type;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Category() {}

    public Category(Long id, String name, String iconName, String color, Boolean isDefault, CategoryType type, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.iconName = iconName;
        this.color = color;
        this.isDefault = isDefault;
        this.type = type;
        this.createdAt = createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String name;
        private String iconName;
        private String color;
        private Boolean isDefault = false;
        private CategoryType type;
        private LocalDateTime createdAt = LocalDateTime.now();

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder iconName(String iconName) { this.iconName = iconName; return this; }
        public Builder color(String color) { this.color = color; return this; }
        public Builder isDefault(Boolean isDefault) { this.isDefault = isDefault; return this; }
        public Builder type(CategoryType type) { this.type = type; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Category build() {
            return new Category(id, name, iconName, color, isDefault, type, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIconName() { return iconName; }
    public void setIconName(String iconName) { this.iconName = iconName; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public Boolean getIsDefault() { return isDefault; }
    public void setIsDefault(Boolean isDefault) { this.isDefault = isDefault; }
    public CategoryType getType() { return type; }
    public void setType(CategoryType type) { this.type = type; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public enum CategoryType {
        EXPENSE, INCOME
    }
}