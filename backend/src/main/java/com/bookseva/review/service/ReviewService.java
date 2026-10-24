package com.bookseva.review.service;
import com.bookseva.order.entity.OrderStatus;


import com.bookseva.order.entity.Order;
import com.bookseva.order.repository.OrderRepository;
import com.bookseva.review.entity.Review;
import com.bookseva.review.dto.ReviewRequest;
import com.bookseva.review.dto.ReviewResponse;
import com.bookseva.review.repository.ReviewRepository;
import com.bookseva.user.entity.User;
import com.bookseva.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReviewResponse addReview(ReviewRequest request, String reviewerEmail) {
        User reviewer = userRepository.findByEmail(reviewerEmail).orElseThrow(() -> new RuntimeException("Reviewer not found"));
        User reviewee = userRepository.findById(request.getRevieweeId()).orElseThrow(() -> new RuntimeException("Reviewee not found"));
        Order order = orderRepository.findById(request.getOrderId()).orElseThrow(() -> new RuntimeException("Order not found"));
        if (order.getStatus() != com.bookseva.order.entity.OrderStatus.COMPLETED) {
            throw new RuntimeException("Order must be completed to leave a review");
        }
        // Ensure review doesn't already exist
        if (reviewRepository.findByOrderIdAndReviewerId(order.getId(), reviewer.getId()).isPresent()) {
            throw new RuntimeException("Review already exists for this order");
        }
        Review review = Review.builder().order(order).reviewer(reviewer).reviewee(reviewee).rating(request.getRating()).comment(request.getComment()).build();
        review = reviewRepository.save(review);
        // Update reviewee's trust score
        int newScore = reviewee.getTrustScore() + ((request.getRating() - 3) * 2);
        reviewee.setTrustScore(Math.max(0, Math.min(100, newScore)));
        userRepository.save(reviewee);
        return mapToResponse(review);
    }

    public List<ReviewResponse> getUserReviews(Long userId) {
        return reviewRepository.findByRevieweeIdOrderByCreatedAtDesc(userId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Long> getMyAuthoredReviewOrderIds(String email) {
        User reviewer = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return reviewRepository.findByReviewerId(reviewer.getId()).stream().map(r -> r.getOrder().getId()).collect(Collectors.toList());
    }

    private ReviewResponse mapToResponse(Review review) {
        return ReviewResponse.builder().id(review.getId()).orderId(review.getOrder().getId()).reviewerId(review.getReviewer().getId()).reviewerName(review.getReviewer().getName()).revieweeId(review.getReviewee().getId()).rating(review.getRating()).comment(review.getComment()).createdAt(review.getCreatedAt()).build();
    }

    public ReviewService(final ReviewRepository reviewRepository, final OrderRepository orderRepository, final UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }
}
