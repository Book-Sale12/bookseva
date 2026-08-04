package com.bookseva.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRequest {
    @NotNull
    private Long orderId;
    
    @NotNull
    private Long revieweeId;
    
    @Min(1)
    @Max(5)
    private int rating;
    
    private String comment;
}
