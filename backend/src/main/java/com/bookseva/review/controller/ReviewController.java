package com.bookseva.review.controller;

import com.bookseva.review.dto.ReviewRequest;
import com.bookseva.review.dto.ReviewResponse;
import com.bookseva.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> addReview(
            @Valid @RequestBody ReviewRequest request,
            Authentication authentication) {
        
        ReviewResponse response = reviewService.addReview(request, authentication.getName());
        return ResponseEntity.ok(Map.of("success", true, "data", response));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserReviews(
            @PathVariable Long userId) {
            
        List<ReviewResponse> response = reviewService.getUserReviews(userId);
        return ResponseEntity.ok(Map.of("success", true, "data", response));
    }
    
    @GetMapping("/my-authored-order-ids")
    public ResponseEntity<?> getMyAuthoredReviewOrderIds(Authentication authentication) {
        return ResponseEntity.ok(Map.of("success", true, "data", reviewService.getMyAuthoredReviewOrderIds(authentication.getName())));
    }
}
