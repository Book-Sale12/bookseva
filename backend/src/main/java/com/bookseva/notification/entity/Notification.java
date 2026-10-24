package com.bookseva.notification.entity;
import com.bookseva.message.entity.Message;


import com.bookseva.user.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;
    @Column(name = "read_status", nullable = false)
    private boolean readStatus;
    @Column(name = "link")
    private String link;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    private static boolean $default$readStatus() {
        return false;
    }


    public static class NotificationBuilder {
        private Long id;
        private User user;
        private String type;
        private String title;
        private String message;
        private boolean readStatus$set;
        private boolean readStatus$value;
        private String link;
        private LocalDateTime createdAt;

        NotificationBuilder() {
        }

        /**
         * @return {@code this}.
         */
        public Notification.NotificationBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Notification.NotificationBuilder user(final User user) {
            this.user = user;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Notification.NotificationBuilder type(final String type) {
            this.type = type;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Notification.NotificationBuilder title(final String title) {
            this.title = title;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Notification.NotificationBuilder message(final String message) {
            this.message = message;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Notification.NotificationBuilder readStatus(final boolean readStatus) {
            this.readStatus$value = readStatus;
            readStatus$set = true;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Notification.NotificationBuilder link(final String link) {
            this.link = link;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Notification.NotificationBuilder createdAt(final LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Notification build() {
            boolean readStatus$value = this.readStatus$value;
            if (!this.readStatus$set) readStatus$value = Notification.$default$readStatus();
            return new Notification(this.id, this.user, this.type, this.title, this.message, readStatus$value, this.link, this.createdAt);
        }

        @Override
        public String toString() {
            return "Notification.NotificationBuilder(id=" + this.id + ", user=" + this.user + ", type=" + this.type + ", title=" + this.title + ", message=" + this.message + ", readStatus$value=" + this.readStatus$value + ", link=" + this.link + ", createdAt=" + this.createdAt + ")";
        }
    }

    public static Notification.NotificationBuilder builder() {
        return new Notification.NotificationBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public User getUser() {
        return this.user;
    }

    public String getType() {
        return this.type;
    }

    public String getTitle() {
        return this.title;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean isReadStatus() {
        return this.readStatus;
    }

    public String getLink() {
        return this.link;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public void setReadStatus(final boolean readStatus) {
        this.readStatus = readStatus;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Notification() {
        this.readStatus = Notification.$default$readStatus();
    }

    public Notification(final Long id, final User user, final String type, final String title, final String message, final boolean readStatus, final String link, final LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.type = type;
        this.title = title;
        this.message = message;
        this.readStatus = readStatus;
        this.link = link;
        this.createdAt = createdAt;
    }
}
