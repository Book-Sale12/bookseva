package com.bookseva.user.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SellerReviewResponse {
    private Long id;
    private String reviewerName;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
}
