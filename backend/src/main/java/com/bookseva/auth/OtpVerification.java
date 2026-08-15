package com.bookseva.auth;

import com.bookseva.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp_verifications", indexes = {
        @Index(name = "idx_otp_user_purpose", columnList = "user_id, purpose")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // We can associate with User if it exists, or just email if it's SIGNUP and user isn't fully created yet.
    // The spec says `user_id`, so we will use a nullable mapping or string if user isn't created.
    // Given OTP verification is mandatory *before* account is active, the user might be in PENDING_VERIFICATION state.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "otp_code_hash", nullable = false)
    private String otpCodeHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OtpPurpose purpose;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    @Builder.Default
    private Integer attempts = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
