package com.bookseva.auth.entity;

import com.bookseva.user.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp_verifications", indexes = {@Index(name = "idx_otp_user_purpose", columnList = "user_id, purpose")})
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
    private Integer attempts;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    private static Integer $default$attempts() {
        return 0;
    }


    public static class OtpVerificationBuilder {
        private Long id;
        private User user;
        private String otpCodeHash;
        private OtpPurpose purpose;
        private LocalDateTime expiresAt;
        private boolean attempts$set;
        private Integer attempts$value;
        private LocalDateTime createdAt;

        OtpVerificationBuilder() {
        }

        /**
         * @return {@code this}.
         */
        public OtpVerification.OtpVerificationBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public OtpVerification.OtpVerificationBuilder user(final User user) {
            this.user = user;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public OtpVerification.OtpVerificationBuilder otpCodeHash(final String otpCodeHash) {
            this.otpCodeHash = otpCodeHash;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public OtpVerification.OtpVerificationBuilder purpose(final OtpPurpose purpose) {
            this.purpose = purpose;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public OtpVerification.OtpVerificationBuilder expiresAt(final LocalDateTime expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public OtpVerification.OtpVerificationBuilder attempts(final Integer attempts) {
            this.attempts$value = attempts;
            attempts$set = true;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public OtpVerification.OtpVerificationBuilder createdAt(final LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public OtpVerification build() {
            Integer attempts$value = this.attempts$value;
            if (!this.attempts$set) attempts$value = OtpVerification.$default$attempts();
            return new OtpVerification(this.id, this.user, this.otpCodeHash, this.purpose, this.expiresAt, attempts$value, this.createdAt);
        }

        @Override
        public String toString() {
            return "OtpVerification.OtpVerificationBuilder(id=" + this.id + ", user=" + this.user + ", otpCodeHash=" + this.otpCodeHash + ", purpose=" + this.purpose + ", expiresAt=" + this.expiresAt + ", attempts$value=" + this.attempts$value + ", createdAt=" + this.createdAt + ")";
        }
    }

    public static OtpVerification.OtpVerificationBuilder builder() {
        return new OtpVerification.OtpVerificationBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public User getUser() {
        return this.user;
    }

    public String getOtpCodeHash() {
        return this.otpCodeHash;
    }

    public OtpPurpose getPurpose() {
        return this.purpose;
    }

    public LocalDateTime getExpiresAt() {
        return this.expiresAt;
    }

    public Integer getAttempts() {
        return this.attempts;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public void setOtpCodeHash(final String otpCodeHash) {
        this.otpCodeHash = otpCodeHash;
    }

    public void setPurpose(final OtpPurpose purpose) {
        this.purpose = purpose;
    }

    public void setExpiresAt(final LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setAttempts(final Integer attempts) {
        this.attempts = attempts;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof OtpVerification)) return false;
        final OtpVerification other = (OtpVerification) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$attempts = this.getAttempts();
        final Object other$attempts = other.getAttempts();
        if (this$attempts == null ? other$attempts != null : !this$attempts.equals(other$attempts)) return false;
        final Object this$user = this.getUser();
        final Object other$user = other.getUser();
        if (this$user == null ? other$user != null : !this$user.equals(other$user)) return false;
        final Object this$otpCodeHash = this.getOtpCodeHash();
        final Object other$otpCodeHash = other.getOtpCodeHash();
        if (this$otpCodeHash == null ? other$otpCodeHash != null : !this$otpCodeHash.equals(other$otpCodeHash)) return false;
        final Object this$purpose = this.getPurpose();
        final Object other$purpose = other.getPurpose();
        if (this$purpose == null ? other$purpose != null : !this$purpose.equals(other$purpose)) return false;
        final Object this$expiresAt = this.getExpiresAt();
        final Object other$expiresAt = other.getExpiresAt();
        if (this$expiresAt == null ? other$expiresAt != null : !this$expiresAt.equals(other$expiresAt)) return false;
        final Object this$createdAt = this.getCreatedAt();
        final Object other$createdAt = other.getCreatedAt();
        if (this$createdAt == null ? other$createdAt != null : !this$createdAt.equals(other$createdAt)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof OtpVerification;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $attempts = this.getAttempts();
        result = result * PRIME + ($attempts == null ? 43 : $attempts.hashCode());
        final Object $user = this.getUser();
        result = result * PRIME + ($user == null ? 43 : $user.hashCode());
        final Object $otpCodeHash = this.getOtpCodeHash();
        result = result * PRIME + ($otpCodeHash == null ? 43 : $otpCodeHash.hashCode());
        final Object $purpose = this.getPurpose();
        result = result * PRIME + ($purpose == null ? 43 : $purpose.hashCode());
        final Object $expiresAt = this.getExpiresAt();
        result = result * PRIME + ($expiresAt == null ? 43 : $expiresAt.hashCode());
        final Object $createdAt = this.getCreatedAt();
        result = result * PRIME + ($createdAt == null ? 43 : $createdAt.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "OtpVerification(id=" + this.getId() + ", user=" + this.getUser() + ", otpCodeHash=" + this.getOtpCodeHash() + ", purpose=" + this.getPurpose() + ", expiresAt=" + this.getExpiresAt() + ", attempts=" + this.getAttempts() + ", createdAt=" + this.getCreatedAt() + ")";
    }

    public OtpVerification() {
        this.attempts = OtpVerification.$default$attempts();
    }

    public OtpVerification(final Long id, final User user, final String otpCodeHash, final OtpPurpose purpose, final LocalDateTime expiresAt, final Integer attempts, final LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.otpCodeHash = otpCodeHash;
        this.purpose = purpose;
        this.expiresAt = expiresAt;
        this.attempts = attempts;
        this.createdAt = createdAt;
    }
}
