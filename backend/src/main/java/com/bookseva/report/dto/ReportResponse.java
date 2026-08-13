package com.bookseva.report.dto;

import lombok.Builder;
import lombok.Data;
import com.bookseva.report.Report;
import java.time.LocalDateTime;

@Data
@Builder
public class ReportResponse {
    private Long id;
    private Long reporterId;
    private Report.TargetType targetType;
    private Long targetId;
    private String reason;
    private Report.ReportStatus status;
    private LocalDateTime createdAt;
}
