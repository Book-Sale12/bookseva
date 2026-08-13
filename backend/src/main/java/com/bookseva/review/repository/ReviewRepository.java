package com.bookseva.review.repository;

import com.bookseva.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByRevieweeIdOrderByCreatedAtDesc(Long revieweeId);
    Optional<Review> findByOrderIdAndReviewerId(Long orderId, Long reviewerId);
    List<Review> findByReviewerId(Long reviewerId);
}
