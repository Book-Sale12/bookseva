package com.bookseva.dispute.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class DisputeCreateRequest {
    @NotBlank(message = "Reason is required")
    private String reason;
    private String description;
    private List<String> evidenceUrls;
}
