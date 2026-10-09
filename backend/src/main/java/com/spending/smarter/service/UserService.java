package com.spending.smarter.service;

import com.spending.smarter.model.User;
import com.spending.smarter.repository.UserRepository;
import com.spending.smarter.dto.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new com.spending.smarter.exception.ApiException("User not found"));
    }

    public User updateProfile(Long userId, String fullName, Double monthlyIncome, String currency) {
        User user = findById(userId);
        if (fullName != null) user.setFullName(fullName);
        if (monthlyIncome != null) user.setMonthlyIncome(monthlyIncome);
        if (currency != null) user.setCurrency(currency);
        user.setUpdatedAt(java.time.LocalDateTime.now());
        return userRepository.save(user);
    }

    public AuthResponse getProfile(Long userId) {
        User user = findById(userId);
        return AuthResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .monthlyIncome(user.getMonthlyIncome())
                .currency(user.getCurrency())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
}