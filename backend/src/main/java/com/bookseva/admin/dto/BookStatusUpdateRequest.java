package com.bookseva.admin.dto;

import com.bookseva.book.ListingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookStatusUpdateRequest {
    @NotNull(message = "New status is required")
    private ListingStatus newStatus;
}
