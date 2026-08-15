package com.bookseva.auth;

import com.bookseva.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {
    Optional<OtpVerification> findTopByUserAndPurposeOrderByCreatedAtDesc(User user, OtpPurpose purpose);

    @Modifying
    @Query("DELETE FROM OtpVerification o WHERE o.user = :user AND o.purpose = :purpose")
    void deleteAllByUserAndPurpose(User user, OtpPurpose purpose);

    @Modifying
    @Query("DELETE FROM OtpVerification o WHERE o.expiresAt < :now")
    void deleteAllExpiredBefore(LocalDateTime now);
}
