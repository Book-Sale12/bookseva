package com.bookseva.admin.service;

import com.bookseva.admin.PlatformSetting;
import com.bookseva.admin.repository.PlatformSettingRepository;
import com.bookseva.book.Book;
import com.bookseva.book.BookRepository;
import com.bookseva.book.ListingStatus;
import com.bookseva.notification.service.NotificationService;
import com.bookseva.report.Report;
import com.bookseva.report.repository.ReportRepository;
import com.bookseva.user.User;
import com.bookseva.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ReportRepository reportRepository;
    private final PlatformSettingRepository platformSettingRepository;
    private final NotificationService notificationService;

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("activeListings", bookRepository.countByStatus(ListingStatus.ACTIVE));
        stats.put("pendingReports", reportRepository.findByStatus(Report.ReportStatus.PENDING).size());
        return stats;
    }

    public List<PlatformSetting> getSettings() {
        return platformSettingRepository.findAll();
    }

    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional
    public void updateUserStatus(Long id, com.bookseva.user.UserStatus newStatus) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(newStatus);
        userRepository.save(user);
    }

    @Transactional
    public void updateBookStatus(Long id, ListingStatus newStatus) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        book.setStatus(newStatus);
        bookRepository.save(book);
    }

    public Object getAdminBooks(String search, Pageable pageable) {
        if (search != null && !search.isBlank()) {
            String pattern = "%" + search.toLowerCase() + "%";
            return bookRepository.findAll((root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("seller").get("name")), pattern)
                    )
            );
        }
        return bookRepository.findAll(pageable);
    }

    public List<Report> getReports() {
        return reportRepository.findAll();
    }

    @Transactional
    public void resolveReport(Long id, Report.ReportStatus newStatus, String message, String adminEmail) {
        Report report = reportRepository.findById(id).orElseThrow(() -> new RuntimeException("Report not found"));
        User admin = userRepository.findByEmail(adminEmail).orElseThrow(() -> new RuntimeException("Admin not found"));

        report.setStatus(newStatus);
        report.setResolvedBy(admin);
        report.setResolvedAt(java.time.LocalDateTime.now());
        reportRepository.save(report);

        String notificationMessage = message != null && !message.trim().isEmpty()
                ? message
                : "Your report has been reviewed and resolved.";

        notificationService.sendNotification(
                report.getReporter(),
                "REPORT_RESOLVED",
                "Report Update",
                notificationMessage,
                "/profile"
        );
    }

    @Transactional
    public void updateSetting(String key, String newValue) {
        PlatformSetting setting = platformSettingRepository.findById(key).orElseThrow(() -> new RuntimeException("Setting not found"));
        setting.setValue(newValue);
        platformSettingRepository.save(setting);
    }
}
