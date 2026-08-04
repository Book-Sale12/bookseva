package com.bookseva.review.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ReviewResponse {
    private Long id;
    private Long orderId;
    private Long reviewerId;
    private String reviewerName;
    private Long revieweeId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
}
