package com.spending.smarter.controller;

import com.spending.smarter.dto.IncomeRequest;
import com.spending.smarter.dto.IncomeResponse;
import com.spending.smarter.service.IncomeService;
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
@RequestMapping("/api/incomes")
@RequiredArgsConstructor
public class IncomeController {
    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<IncomeResponse> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody IncomeRequest request) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(incomeService.create(userId, request));
    }

    @GetMapping
    public ResponseEntity<List<IncomeResponse>> getByDateRange(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(incomeService.getByDateRange(userId, start, end));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<IncomeResponse>> getByDate(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable LocalDate date) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(incomeService.getByDate(userId, date));
    }

    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getTotal(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(incomeService.getTotalIncome(userId, start, end));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncomeResponse> update(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody IncomeRequest request) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(incomeService.update(userId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        Long userId = getUserId(userDetails);
        incomeService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }

    private Long getUserId(UserDetails userDetails) {
        return ((com.spending.smarter.model.User) userDetails).getId();
    }
}