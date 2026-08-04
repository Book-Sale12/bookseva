package com.bookseva.user.controller;

import com.bookseva.user.User;
import com.bookseva.user.UserRepository;
import com.bookseva.user.dto.UserUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(Map.of("success", true, "data", user));
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateCurrentUser(
            @Valid @RequestBody UserUpdateRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setCollegeName(request.getCollegeName());
        user.setCourseBranch(request.getCourseBranch());

        userRepository.save(user);

        return ResponseEntity.ok(Map.of("success", true, "data", user));
    }
}
