package com.spending.smarter.dto;

import com.spending.smarter.model.Category;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {
    private Long id;
    private String name;
    private String iconName;
    private String color;
    private Boolean isDefault;
    private String type;

    public static CategoryResponse from(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .iconName(category.getIconName())
                .color(category.getColor())
                .isDefault(category.getIsDefault())
                .type(category.getType().name())
                .build();
    }
}