package com.bookseva.report.controller;
import com.bookseva.report.entity.Report;


import com.bookseva.report.dto.ReportRequest;
import com.bookseva.report.dto.ReportResponse;
import com.bookseva.report.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {
    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<?> submitReport(@Valid @RequestBody ReportRequest request, Authentication authentication) {
        ReportResponse response = reportService.submitReport(request, authentication.getName());
        return ResponseEntity.ok(Map.of("success", true, "data", response));
    }

    public ReportController(final ReportService reportService) {
        this.reportService = reportService;
    }
}
