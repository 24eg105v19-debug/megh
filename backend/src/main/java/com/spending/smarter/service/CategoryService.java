package com.spending.smarter.service;

import com.spending.smarter.model.Category;
import com.spending.smarter.repository.CategoryRepository;
import com.spending.smarter.dto.CategoryResponse;
import com.spending.smarter.exception.ApiException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> getCategoriesByType(Category.CategoryType type) {
        return categoryRepository.findByType(type).stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ApiException("Category not found"));
    }

    public void seedDefaultCategories() {
        if (categoryRepository.count() > 0) return;

        String[][] expenseCategories = {
            {"Food & Dining", "utensils", "#FF6B6B", "EXPENSE"},
            {"Transportation", "car", "#4ECDC4", "EXPENSE"},
            {"Shopping", "shopping-bag", "#45B7D1", "EXPENSE"},
            {"Entertainment", "film", "#96CEB4", "EXPENSE"},
            {"Bills & Utilities", "file-text", "#FFEAA7", "EXPENSE"},
            {"Healthcare", "heart", "#DDA0DD", "EXPENSE"},
            {"Education", "book", "#98D8C8", "EXPENSE"},
            {"Travel", "plane", "#F7DC6F", "EXPENSE"},
            {"Personal Care", "smile", "#BB8FCE", "EXPENSE"},
            {"Other", "more-horizontal", "#85C1E9", "EXPENSE"}
        };

        String[][] incomeCategories = {
            {"Salary", "briefcase", "#2ECC71", "INCOME"},
            {"Freelance", "laptop", "#27AE60", "INCOME"},
            {"Investments", "trending-up", "#1ABC9C", "INCOME"},
            {"Gifts", "gift", "#16A085", "INCOME"},
            {"Other", "more-horizontal", "#138D75", "INCOME"}
        };

        for (String[] cat : expenseCategories) {
            Category category = Category.builder()
                    .name(cat[0])
                    .iconName(cat[1])
                    .color(cat[2])
                    .type(Category.CategoryType.valueOf(cat[3]))
                    .isDefault(true)
                    .build();
            categoryRepository.save(category);
        }

        for (String[] cat : incomeCategories) {
            Category category = Category.builder()
                    .name(cat[0])
                    .iconName(cat[1])
                    .color(cat[2])
                    .type(Category.CategoryType.valueOf(cat[3]))
                    .isDefault(true)
                    .build();
            categoryRepository.save(category);
        }
    }
}