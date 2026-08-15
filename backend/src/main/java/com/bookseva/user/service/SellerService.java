package com.bookseva.user.service;

import com.bookseva.book.Book;
import com.bookseva.book.BookRepository;
import com.bookseva.book.ListingStatus;
import com.bookseva.review.Review;
import com.bookseva.review.repository.ReviewRepository;
import com.bookseva.user.User;
import com.bookseva.user.UserRepository;
import com.bookseva.user.dto.SellerProfileResponse;
import com.bookseva.user.dto.SellerReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public SellerProfileResponse getSellerProfile(Long sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        Double averageRating = reviewRepository.getAverageRatingByRevieweeId(sellerId);
        long totalReviews = reviewRepository.countByRevieweeId(sellerId);
        
        long totalBooksListed = bookRepository.countBySellerId(sellerId);
        long activeListings = bookRepository.countBySellerIdAndStatus(sellerId, ListingStatus.ACTIVE);
        long totalBooksSold = bookRepository.countBySellerIdAndStatus(sellerId, ListingStatus.SOLD);

        return SellerProfileResponse.builder()
                .id(seller.getId())
                .name(seller.getName())
                .collegeName(seller.getCollegeName())
                .status(seller.getStatus().name())
                .trustScore(seller.getTrustScore())
                .averageRating(averageRating)
                .totalReviews(totalReviews)
                .totalBooksListed(totalBooksListed)
                .totalBooksSold(totalBooksSold)
                .activeListings(activeListings)
                .build();
    }

    @Transactional(readOnly = true)
    public Page<SellerReviewResponse> getSellerReviews(Long sellerId, Pageable pageable) {
        // Ensure seller exists
        if (!userRepository.existsById(sellerId)) {
            throw new RuntimeException("Seller not found");
        }

        Page<Review> reviews = reviewRepository.findByRevieweeIdOrderByCreatedAtDesc(sellerId, pageable);
        return reviews.map(review -> SellerReviewResponse.builder()
                .id(review.getId())
                .reviewerName(review.getReviewer().getName())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build());
    }

    @Transactional(readOnly = true)
    public List<Book> getSellerActiveListings(Long sellerId) {
        // Ensure seller exists
        if (!userRepository.existsById(sellerId)) {
            throw new RuntimeException("Seller not found");
        }

        return bookRepository.findBySellerIdAndStatusOrderByCreatedAtDesc(sellerId, ListingStatus.ACTIVE);
    }
}
