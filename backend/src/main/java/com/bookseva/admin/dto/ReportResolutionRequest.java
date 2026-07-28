package com.bookseva.admin.dto;

import com.bookseva.report.Report.ReportStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReportResolutionRequest {
    @NotNull(message = "New status is required")
    private ReportStatus newStatus;

    private String message;
}
