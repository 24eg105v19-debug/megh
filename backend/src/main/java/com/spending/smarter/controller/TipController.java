package com.spending.smarter.controller;

import com.spending.smarter.dto.TipResponse;
import com.spending.smarter.service.TipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tips")
@RequiredArgsConstructor
public class TipController {
    private final TipService tipService;

    @GetMapping
    public ResponseEntity<List<TipResponse>> getAll(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(tipService.getAll(getUserId(userDetails)));
    }

    @PostMapping("/generate")
    public ResponseEntity<List<TipResponse>> generate(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(tipService.generate(getUserId(userDetails)));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<TipResponse> markRead(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        return ResponseEntity.ok(tipService.markRead(getUserId(userDetails), id));
    }

    private Long getUserId(UserDetails userDetails) {
        return ((com.spending.smarter.model.User) userDetails).getId();
    }
}