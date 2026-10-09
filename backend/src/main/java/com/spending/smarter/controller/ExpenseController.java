package com.spending.smarter.controller;

import com.spending.smarter.dto.ExpenseRequest;
import com.spending.smarter.dto.ExpenseResponse;
import com.spending.smarter.dto.CategoryExpenseSummary;
import com.spending.smarter.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponse> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ExpenseRequest request) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(expenseService.create(userId, request));
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getByDateRange(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(expenseService.getByDateRange(userId, start, end));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<ExpenseResponse>> getByDate(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable LocalDate date) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(expenseService.getByDate(userId, date));
    }

    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getTotal(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(expenseService.getTotalExpenses(userId, start, end));
    }

    @GetMapping("/by-category")
    public ResponseEntity<List<CategoryExpenseSummary>> getByCategory(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(expenseService.getExpensesByCategory(userId, start, end));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> update(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequest request) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(expenseService.update(userId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        Long userId = getUserId(userDetails);
        expenseService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }

    private Long getUserId(UserDetails userDetails) {
        return ((com.spending.smarter.model.User) userDetails).getId();
    }
}