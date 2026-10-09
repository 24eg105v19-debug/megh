package com.spending.smarter.service;

import com.spending.smarter.model.Income;
import com.spending.smarter.model.User;
import com.spending.smarter.model.Category;
import com.spending.smarter.repository.IncomeRepository;
import com.spending.smarter.repository.CategoryRepository;
import com.spending.smarter.dto.IncomeRequest;
import com.spending.smarter.dto.IncomeResponse;
import com.spending.smarter.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncomeService {
    private final IncomeRepository incomeRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public IncomeResponse create(Long userId, IncomeRequest request) {
        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ApiException("Category not found"));
            if (category.getType() != Category.CategoryType.INCOME) {
                throw new ApiException("Invalid category type for income");
            }
        }

        Income income = Income.builder()
                .user(User.builder().id(userId).build())
                .category(category)
                .amount(request.getAmount())
                .incomeDate(request.getIncomeDate())
                .description(request.getDescription())
                .isRecurring(request.getIsRecurring())
                .recurringFrequency(request.getRecurringFrequency() != null 
                        ? Income.RecurringFrequency.valueOf(request.getRecurringFrequency()) 
                        : null)
                .build();

        income = incomeRepository.save(income);
        return IncomeResponse.from(income);
    }

    public List<IncomeResponse> getByDateRange(Long userId, LocalDate start, LocalDate end) {
        User user = User.builder().id(userId).build();
        return incomeRepository.findByUserAndIncomeDateBetween(user, start, end).stream()
                .map(IncomeResponse::from)
                .collect(Collectors.toList());
    }

    public List<IncomeResponse> getByDate(Long userId, LocalDate date) {
        User user = User.builder().id(userId).build();
        return incomeRepository.findByUserAndIncomeDate(user, date).stream()
                .map(IncomeResponse::from)
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalIncome(Long userId, LocalDate start, LocalDate end) {
        User user = User.builder().id(userId).build();
        BigDecimal total = incomeRepository.getTotalIncomeByUserAndDateRange(user, start, end);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Transactional
    public IncomeResponse update(Long userId, Long incomeId, IncomeRequest request) {
        Income income = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new ApiException("Income not found"));
        
        if (!income.getUser().getId().equals(userId)) {
            throw new ApiException("Unauthorized");
        }

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ApiException("Category not found"));
            if (category.getType() != Category.CategoryType.INCOME) {
                throw new ApiException("Invalid category type for income");
            }
        }

        income.setCategory(category);
        income.setAmount(request.getAmount());
        income.setIncomeDate(request.getIncomeDate());
        income.setDescription(request.getDescription());
        income.setIsRecurring(request.getIsRecurring());
        income.setRecurringFrequency(request.getRecurringFrequency() != null 
                ? Income.RecurringFrequency.valueOf(request.getRecurringFrequency()) 
                : null);
        income.setUpdatedAt(java.time.LocalDateTime.now());

        income = incomeRepository.save(income);
        return IncomeResponse.from(income);
    }

    @Transactional
    public void delete(Long userId, Long incomeId) {
        Income income = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new ApiException("Income not found"));
        
        if (!income.getUser().getId().equals(userId)) {
            throw new ApiException("Unauthorized");
        }

        incomeRepository.delete(income);
    }

    public List<Income> getRecurringIncomes(Long userId) {
        User user = User.builder().id(userId).build();
        return incomeRepository.findByUserAndIsRecurringTrue(user);
    }
}