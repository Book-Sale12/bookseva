package com.bookseva.admin.controller;

import com.bookseva.admin.PlatformSetting;
import com.bookseva.admin.repository.PlatformSettingRepository;
import com.bookseva.book.BookRepository;
import com.bookseva.notification.service.NotificationService;
import com.bookseva.report.Report;
import com.bookseva.report.repository.ReportRepository;
import com.bookseva.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ReportRepository reportRepository;
    private final PlatformSettingRepository platformSettingRepository;
    private final com.bookseva.dispute.service.DisputeService disputeService;
    private final NotificationService notificationService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("activeListings", bookRepository.count());
        stats.put("pendingReports", reportRepository.findByStatus(Report.ReportStatus.PENDING).size());
        
        return ResponseEntity.ok(Map.of("success", true, "data", stats));
    }

    @GetMapping("/settings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getSettings() {
        return ResponseEntity.ok(Map.of("success", true, "data", platformSettingRepository.findAll()));
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(Map.of("success", true, "data", userRepository.findAll()));
    }

    @PatchMapping("/users/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long id, @jakarta.validation.Valid @RequestBody com.bookseva.admin.dto.UserStatusUpdateRequest request) {
        com.bookseva.user.User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(request.getNewStatus());
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("success", true, "data", "User status updated"));
    }

    @PatchMapping("/books/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateBookStatus(@PathVariable Long id, @jakarta.validation.Valid @RequestBody com.bookseva.admin.dto.BookStatusUpdateRequest request) {
        com.bookseva.book.Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        book.setStatus(request.getNewStatus());
        bookRepository.save(book);
        return ResponseEntity.ok(Map.of("success", true, "data", "Book status updated"));
    }

    @GetMapping("/books")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAdminBooks(@RequestParam(required = false) String search) {
        List<com.bookseva.book.Book> books;
        if (search != null && !search.isBlank()) {
            String pattern = "%" + search.toLowerCase() + "%";
            books = bookRepository.findAll((root, query, criteriaBuilder) -> 
                criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("seller").get("name")), pattern)
                )
            );
        } else {
            books = bookRepository.findAll();
        }
        return ResponseEntity.ok(Map.of("success", true, "data", books));
    }

    @GetMapping("/reports")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getReports() {
        return ResponseEntity.ok(Map.of("success", true, "data", reportRepository.findAll()));
    }

    @PatchMapping("/reports/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> resolveReport(@PathVariable Long id, @jakarta.validation.Valid @RequestBody com.bookseva.admin.dto.ReportResolutionRequest request, org.springframework.security.core.Authentication auth) {
        Report report = reportRepository.findById(id).orElseThrow(() -> new RuntimeException("Report not found"));
        com.bookseva.user.User admin = userRepository.findByEmail(auth.getName()).orElseThrow(() -> new RuntimeException("Admin not found"));
        
        report.setStatus(request.getNewStatus());
        report.setResolvedBy(admin);
        report.setResolvedAt(java.time.LocalDateTime.now());
        reportRepository.save(report);

        String message = request.getMessage() != null && !request.getMessage().trim().isEmpty() 
            ? request.getMessage() 
            : "Your report has been reviewed and resolved.";

        notificationService.sendNotification(
            report.getReporter(),
            "REPORT_RESOLVED",
            "Report Update",
            message,
            "/profile"
        );

        return ResponseEntity.ok(Map.of("success", true, "data", "Report resolved"));
    }

    @PutMapping("/settings/{key}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSetting(@PathVariable String key, @jakarta.validation.Valid @RequestBody com.bookseva.admin.dto.SettingUpdateRequest request) {
        PlatformSetting setting = platformSettingRepository.findById(key).orElseThrow(() -> new RuntimeException("Setting not found"));
        setting.setValue(request.getNewValue());
        platformSettingRepository.save(setting);
        return ResponseEntity.ok(Map.of("success", true, "data", "Setting updated"));
    }

    @GetMapping("/disputes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getDisputes() {
        return ResponseEntity.ok(Map.of("success", true, "data", disputeService.getAllDisputes()));
    }

    @PatchMapping("/disputes/{id}/resolve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> resolveDispute(
            @PathVariable Long id,
            @jakarta.validation.Valid @RequestBody com.bookseva.dispute.dto.DisputeResolveRequest request,
            org.springframework.security.core.Authentication auth) {
        var dispute = disputeService.resolveDispute(id, request, auth.getName());
        return ResponseEntity.ok(Map.of("success", true, "data", dispute));
    }
}
