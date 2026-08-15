package com.bookseva.auth.service;

import com.bookseva.auth.OtpPurpose;
import com.bookseva.auth.OtpVerification;
import com.bookseva.auth.OtpVerificationRepository;
import com.bookseva.auth.dto.AuthResponse;
import com.bookseva.auth.dto.LoginRequest;
import com.bookseva.auth.dto.RegisterRequest;
import com.bookseva.auth.dto.ResendOtpRequest;
import com.bookseva.auth.dto.VerifyOtpRequest;
import com.bookseva.auth.dto.ForgotPasswordRequest;
import com.bookseva.auth.dto.ResetPasswordRequest;
import com.bookseva.common.exception.UnverifiedAccountException;
import com.bookseva.notification.service.MailService;
import com.bookseva.user.Role;
import com.bookseva.user.User;
import com.bookseva.user.UserRepository;
import com.bookseva.user.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final OtpVerificationRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MailService mailService;


    @Transactional
    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .collegeName(request.getCollegeName())
                .courseBranch(request.getCourseBranch())
                .role(Role.STUDENT)
                .status(UserStatus.PENDING_VERIFICATION)
                .build();
        
        userRepository.save(user);

        sendOtp(user, OtpPurpose.SIGNUP);
    }

    @Transactional
    public void sendOtp(User user, OtpPurpose purpose) {
        Optional<OtpVerification> recentOtp = otpRepository.findTopByUserAndPurposeOrderByCreatedAtDesc(user, purpose);
        if (recentOtp.isPresent() && recentOtp.get().getCreatedAt().plusMinutes(1).isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Please wait 1 minute before requesting a new OTP.");
        }

        // Invalidate all previous OTPs for this user+purpose before generating a new one
        otpRepository.deleteAllByUserAndPurpose(user, purpose);

        String otp = String.format("%06d", new SecureRandom().nextInt(999999));
        
        OtpVerification otpVerification = OtpVerification.builder()
                .user(user)
                .otpCodeHash(passwordEncoder.encode(otp))
                .purpose(purpose)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();

        otpRepository.save(otpVerification);

        String subject = "BookSeva Verification Code";
        String text = "Your verification code is: " + otp + "\nIt will expire in 10 minutes.";
        mailService.sendEmail(user.getEmail(), subject, text);
    }

    @Transactional
    public AuthResponse verifyOtp(VerifyOtpRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        OtpVerification otpVerification = otpRepository.findTopByUserAndPurposeOrderByCreatedAtDesc(user, OtpPurpose.SIGNUP)
                .orElseThrow(() -> new RuntimeException("No active OTP found"));

        if (otpVerification.getAttempts() >= 5) {
            throw new RuntimeException("Maximum OTP attempts reached");
        }

        if (LocalDateTime.now().isAfter(otpVerification.getExpiresAt())) {
            throw new RuntimeException("OTP has expired");
        }

        if (!passwordEncoder.matches(request.getOtp(), otpVerification.getOtpCodeHash())) {
            otpVerification.setAttempts(otpVerification.getAttempts() + 1);
            otpRepository.save(otpVerification);
            throw new RuntimeException("Invalid OTP");
        }

        // Mark user active
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        return generateAuthResponse(user);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            if (user.getStatus() == UserStatus.PENDING_VERIFICATION) {
                // Optionally resend OTP
                sendOtp(user, OtpPurpose.SIGNUP);
                throw new UnverifiedAccountException("Account not verified. A new OTP has been sent to your email.");
            }
            throw new RuntimeException("Account is " + user.getStatus());
        }

        return generateAuthResponse(user);
    }

    @Transactional
    public void resendOtp(ResendOtpRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getStatus() != UserStatus.PENDING_VERIFICATION) {
            throw new RuntimeException("User is already verified or not pending verification");
        }

        // Invalidate old OTPs for signup by generating a new one
        sendOtp(user, OtpPurpose.SIGNUP);
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            // Invalidate old reset OTPs by generating a new one
            sendOtp(user, OtpPurpose.RESET);
        });
        // We always return success silently without revealing if the user exists
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid request"));

        OtpVerification otpVerification = otpRepository.findTopByUserAndPurposeOrderByCreatedAtDesc(user, OtpPurpose.RESET)
                .orElseThrow(() -> new RuntimeException("No active reset request found"));

        if (otpVerification.getAttempts() >= 5) {
            throw new RuntimeException("Maximum OTP attempts reached");
        }

        if (LocalDateTime.now().isAfter(otpVerification.getExpiresAt())) {
            throw new RuntimeException("OTP has expired");
        }

        if (!passwordEncoder.matches(request.getOtp(), otpVerification.getOtpCodeHash())) {
            otpVerification.setAttempts(otpVerification.getAttempts() + 1);
            otpRepository.save(otpVerification);
            throw new RuntimeException("Invalid OTP");
        }

        // Valid OTP, reset password
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Delete the OTP to prevent replay attacks
        otpRepository.delete(otpVerification);
    }

    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new RuntimeException("Refresh token is missing");
        }
        
        String userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail == null) {
            throw new RuntimeException("Invalid refresh token");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new RuntimeException("Refresh token is invalid or expired");
        }

        return generateAuthResponse(user);
    }

    private AuthResponse generateAuthResponse(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        AuthResponse.UserDto userDto = AuthResponse.UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .trustScore(user.getTrustScore())
                .build();

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userDto)
                .build();
    }
}
