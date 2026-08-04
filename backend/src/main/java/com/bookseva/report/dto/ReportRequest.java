package com.bookseva.report.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.bookseva.report.Report;

@Data
public class ReportRequest {
    @NotNull
    private Report.TargetType targetType;
    
    @NotNull
    private Long targetId;
    
    @NotBlank
    private String reason;
}
