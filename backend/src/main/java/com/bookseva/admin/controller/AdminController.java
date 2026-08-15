package com.bookseva.admin.controller;

import com.bookseva.admin.service.AdminService;
import com.bookseva.dispute.service.DisputeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final DisputeService disputeService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getDashboardStats() {
        return ResponseEntity.ok(Map.of("success", true, "data", adminService.getDashboardStats()));
    }

    @GetMapping("/settings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getSettings() {
        return ResponseEntity.ok(Map.of("success", true, "data", adminService.getSettings()));
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        size = Math.min(size, 100);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, org.springframework.data.domain.Sort.by("id").descending());
        return ResponseEntity.ok(Map.of("success", true, "data", adminService.getUsers(pageable).getContent()));
    }

    @PatchMapping("/users/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long id, @jakarta.validation.Valid @RequestBody com.bookseva.admin.dto.UserStatusUpdateRequest request) {
        adminService.updateUserStatus(id, request.getNewStatus());
        return ResponseEntity.ok(Map.of("success", true, "data", "User status updated"));
    }

    @PatchMapping("/books/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateBookStatus(@PathVariable Long id, @jakarta.validation.Valid @RequestBody com.bookseva.admin.dto.BookStatusUpdateRequest request) {
        adminService.updateBookStatus(id, request.getNewStatus());
        return ResponseEntity.ok(Map.of("success", true, "data", "Book status updated"));
    }

    @GetMapping("/books")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAdminBooks(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        size = Math.min(size, 100);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, org.springframework.data.domain.Sort.by("id").descending());
        var booksResult = adminService.getAdminBooks(search, pageable);
        var booksList = (booksResult instanceof org.springframework.data.domain.Page<?> p) ? p.getContent() : booksResult;
        return ResponseEntity.ok(Map.of("success", true, "data", booksList));
    }

    @GetMapping("/reports")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getReports() {
        return ResponseEntity.ok(Map.of("success", true, "data", adminService.getReports()));
    }

    @PatchMapping("/reports/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> resolveReport(@PathVariable Long id, @jakarta.validation.Valid @RequestBody com.bookseva.admin.dto.ReportResolutionRequest request, org.springframework.security.core.Authentication auth) {
        adminService.resolveReport(id, request.getNewStatus(), request.getMessage(), auth.getName());
        return ResponseEntity.ok(Map.of("success", true, "data", "Report resolved"));
    }

    @PutMapping("/settings/{key}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSetting(@PathVariable String key, @jakarta.validation.Valid @RequestBody com.bookseva.admin.dto.SettingUpdateRequest request) {
        adminService.updateSetting(key, request.getNewValue());
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
