package com.bookseva.review.repository;

import com.bookseva.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByRevieweeIdOrderByCreatedAtDesc(Long revieweeId);
    Page<Review> findByRevieweeIdOrderByCreatedAtDesc(Long revieweeId, Pageable pageable);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.reviewee.id = :revieweeId")
    Double getAverageRatingByRevieweeId(@Param("revieweeId") Long revieweeId);

    Optional<Review> findByOrderIdAndReviewerId(Long orderId, Long reviewerId);
    List<Review> findByReviewerId(Long reviewerId);
    long countByRevieweeId(Long revieweeId);
}
