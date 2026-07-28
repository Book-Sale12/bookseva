package com.bookseva.report.service;

import com.bookseva.notification.service.NotificationService;
import com.bookseva.report.Report;
import com.bookseva.report.dto.ReportRequest;
import com.bookseva.report.dto.ReportResponse;
import com.bookseva.report.repository.ReportRepository;
import com.bookseva.user.User;
import com.bookseva.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public ReportResponse submitReport(ReportRequest request, String reporterEmail) {
        User reporter = userRepository.findByEmail(reporterEmail)
                .orElseThrow(() -> new RuntimeException("Reporter not found"));

        if (reportRepository.existsByReporterIdAndTargetTypeAndTargetId(reporter.getId(), request.getTargetType(), request.getTargetId())) {
            throw new RuntimeException("You have already reported this " + request.getTargetType().name().toLowerCase());
        }

        Report report = Report.builder()
                .reporter(reporter)
                .targetType(request.getTargetType())
                .targetId(request.getTargetId())
                .reason(request.getReason())
                .status(Report.ReportStatus.PENDING)
                .build();
                
        report = reportRepository.save(report);
        
        notificationService.sendNotification(
            reporter,
            "REPORT_SUBMITTED",
            "Report Received",
            "We've received your report and are reviewing it.",
            "/profile"
        );
        
        return mapToResponse(report);
    }

    private ReportResponse mapToResponse(Report report) {
        return ReportResponse.builder()
                .id(report.getId())
                .reporterId(report.getReporter().getId())
                .targetType(report.getTargetType())
                .targetId(report.getTargetId())
                .reason(report.getReason())
                .status(report.getStatus())
                .createdAt(report.getCreatedAt())
                .build();
    }
}
