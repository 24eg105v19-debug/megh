package com.spending.smarter.config;

import com.spending.smarter.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final CategoryService categoryService;

    @Override
    public void run(String... args) {
        categoryService.seedDefaultCategories();
    }
}