package com.spending.smarter.dto;

import com.spending.smarter.model.Tip;
import java.time.LocalDateTime;

public class TipResponse {
    private Long id;
    private String category;
    private String priorityLevel;
    private String tip;
    private Boolean isRead;
    private LocalDateTime createdAt;

    public TipResponse() {}

    public TipResponse(Long id, String category, String priorityLevel, String tip, Boolean isRead, LocalDateTime createdAt) {
        this.id = id;
        this.category = category;
        this.priorityLevel = priorityLevel;
        this.tip = tip;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String category;
        private String priorityLevel;
        private String tip;
        private Boolean isRead;
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder category(String category) { this.category = category; return this; }
        public Builder priorityLevel(String priorityLevel) { this.priorityLevel = priorityLevel; return this; }
        public Builder tip(String tip) { this.tip = tip; return this; }
        public Builder isRead(Boolean isRead) { this.isRead = isRead; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public TipResponse build() {
            return new TipResponse(id, category, priorityLevel, tip, isRead, createdAt);
        }
    }

    public static TipResponse from(Tip tip) {
        return TipResponse.builder()
                .id(tip.getId())
                .category(tip.getCategory())
                .priorityLevel(tip.getPriorityLevel())
                .tip(tip.getTip())
                .isRead(tip.getIsRead())
                .createdAt(tip.getCreatedAt())
                .build();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getPriorityLevel() { return priorityLevel; }
    public void setPriorityLevel(String priorityLevel) { this.priorityLevel = priorityLevel; }
    public String getTip() { return tip; }
    public void setTip(String tip) { this.tip = tip; }
    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}