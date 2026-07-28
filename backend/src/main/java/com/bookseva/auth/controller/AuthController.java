package com.bookseva.auth.controller;

import com.bookseva.auth.dto.AuthResponse;
import com.bookseva.auth.dto.LoginRequest;
import com.bookseva.auth.dto.RegisterRequest;
import com.bookseva.auth.dto.VerifyOtpRequest;
import com.bookseva.auth.dto.ResendOtpRequest;
import com.bookseva.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok(Map.of("success", true, "message", "Registration successful. OTP sent to email."));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyOtpRequest request, HttpServletResponse httpServletResponse) {
        AuthResponse response = authService.verifyOtp(request);
        setRefreshTokenCookie(httpServletResponse, response.getRefreshToken());
        return ResponseEntity.ok(Map.of("success", true, "data", response));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletResponse httpServletResponse) {
        AuthResponse response = authService.login(request);
        setRefreshTokenCookie(httpServletResponse, response.getRefreshToken());
        return ResponseEntity.ok(Map.of("success", true, "data", response));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@Valid @RequestBody ResendOtpRequest request) {
        authService.resendOtp(request);
        return ResponseEntity.ok(Map.of("success", true, "message", "OTP resent successfully."));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody com.bookseva.auth.dto.ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok(Map.of("success", true, "message", "If your email is registered, you will receive a reset OTP shortly."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody com.bookseva.auth.dto.ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(Map.of("success", true, "message", "Password reset successfully. You can now login."));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(name = "refreshToken", required = false) String refreshToken, HttpServletResponse httpServletResponse) {
        AuthResponse response = authService.refreshToken(refreshToken);
        setRefreshTokenCookie(httpServletResponse, response.getRefreshToken());
        return ResponseEntity.ok(Map.of("success", true, "data", response));
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) // Set to true in production
                .path("/api/v1/auth/refresh")
                .maxAge(7 * 24 * 60 * 60) // 7 days
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
