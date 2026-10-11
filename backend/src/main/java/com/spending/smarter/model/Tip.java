package com.spending.smarter.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tips")
public class Tip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 50)
    private String category;

    @Column(columnDefinition = "TEXT")
    private String tip;

    @Column(name = "priority_level", length = 10)
    private String priorityLevel;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Tip() {}

    public Tip(Long id, User user, String category, String tip, String priorityLevel, Boolean isRead, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.category = category;
        this.tip = tip;
        this.priorityLevel = priorityLevel;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private User user;
        private String category;
        private String tip;
        private String priorityLevel;
        private Boolean isRead = false;
        private LocalDateTime createdAt = LocalDateTime.now();

        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder category(String category) { this.category = category; return this; }
        public Builder tip(String tip) { this.tip = tip; return this; }
        public Builder priorityLevel(String priorityLevel) { this.priorityLevel = priorityLevel; return this; }
        public Builder isRead(Boolean isRead) { this.isRead = isRead; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Tip build() {
            return new Tip(id, user, category, tip, priorityLevel, isRead, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getTip() { return tip; }
    public void setTip(String tip) { this.tip = tip; }
    public String getPriorityLevel() { return priorityLevel; }
    public void setPriorityLevel(String priorityLevel) { this.priorityLevel = priorityLevel; }
    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}