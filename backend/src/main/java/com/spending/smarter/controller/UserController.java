package com.spending.smarter.controller;

import com.spending.smarter.dto.AuthResponse;
import com.spending.smarter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<AuthResponse> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(userService.getProfile(userId));
    }

    @PutMapping("/profile")
    public ResponseEntity<AuthResponse> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) Double monthlyIncome,
            @RequestParam(required = false) String currency) {
        Long userId = getUserId(userDetails);
        userService.updateProfile(userId, fullName, monthlyIncome, currency);
        return ResponseEntity.ok(userService.getProfile(userId));
    }

    private Long getUserId(UserDetails userDetails) {
        return ((com.spending.smarter.model.User) userDetails).getId();
    }
}