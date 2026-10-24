package com.bookseva.notification.service;
import com.bookseva.message.entity.Message;


import com.bookseva.notification.entity.Notification;
import com.bookseva.notification.repository.NotificationRepository;
import com.bookseva.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Transactional
    public void sendNotification(User user, String type, String title, String message, String link) {
        Notification notification = Notification.builder().user(user).type(type).title(title).message(message).link(link).build();
        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<Notification> getMyNotifications(String email) {
        return notificationRepository.findByUserEmailOrderByCreatedAtDesc(email);
    }

    @Transactional(readOnly = true)
    public int getUnreadCount(String email) {
        return notificationRepository.countByUserEmailAndReadStatusFalse(email);
    }

    @Transactional
    public void markAsRead(Long id, String email) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Notification not found"));
        if (!notification.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }
        notification.setReadStatus(true);
        notificationRepository.save(notification);
    }

    public NotificationService(final NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }
}
