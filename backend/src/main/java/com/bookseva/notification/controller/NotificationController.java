package com.bookseva.notification.controller;

import com.bookseva.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<?> getMyNotifications(Authentication authentication) {
        var notifications = notificationService.getMyNotifications(authentication.getName());
        return ResponseEntity.ok(Map.of("success", true, "data", notifications));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<?> getUnreadCount(Authentication authentication) {
        var count = notificationService.getUnreadCount(authentication.getName());
        return ResponseEntity.ok(Map.of("success", true, "data", count));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id, Authentication authentication) {
        notificationService.markAsRead(id, authentication.getName());
        return ResponseEntity.ok(Map.of("success", true, "data", "Notification marked as read"));
    }
}
