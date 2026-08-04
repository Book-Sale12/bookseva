package com.bookseva.dispute.dto;

import com.bookseva.dispute.Dispute.DisputeStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DisputeResolveRequest {
    @NotNull(message = "New status is required")
    private DisputeStatus newStatus;
    private String resolutionNotes;
}
