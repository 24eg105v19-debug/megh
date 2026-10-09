package com.spending.smarter.controller;

import com.spending.smarter.dto.BudgetRequest;
import com.spending.smarter.dto.BudgetResponse;
import com.spending.smarter.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<BudgetResponse> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody BudgetRequest request) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(budgetService.create(userId, request));
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getAll(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(budgetService.getAllBudgets(userId));
    }

    @GetMapping("/active")
    public ResponseEntity<List<BudgetResponse>> getActive(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) LocalDate date) {
        Long userId = getUserId(userDetails);
        LocalDate targetDate = date != null ? date : LocalDate.now();
        return ResponseEntity.ok(budgetService.getActiveBudgets(userId, targetDate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponse> update(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody BudgetRequest request) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(budgetService.update(userId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        Long userId = getUserId(userDetails);
        budgetService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }

    private Long getUserId(UserDetails userDetails) {
        return ((com.spending.smarter.model.User) userDetails).getId();
    }
}