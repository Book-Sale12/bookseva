package com.bookseva.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SellerProfileResponse {
    private Long id;
    private String name;
    private String collegeName;
    private String status;
    private Integer trustScore;
    private Double averageRating;
    private long totalReviews;
    private long totalBooksListed;
    private long totalBooksSold;
    private long activeListings;
}
