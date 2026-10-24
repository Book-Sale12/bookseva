package com.bookseva.admin.entity;

import com.bookseva.user.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;
    @Column(nullable = false)
    private String action;
    @Column(name = "target_type", nullable = false)
    private String targetType;
    @Column(name = "target_id", nullable = false)
    private Long targetId;
    @Column(columnDefinition = "TEXT")
    private String details;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    public static class AuditLogBuilder {
        private Long id;
        private User admin;
        private String action;
        private String targetType;
        private Long targetId;
        private String details;
        private LocalDateTime createdAt;

        AuditLogBuilder() {
        }

        /**
         * @return {@code this}.
         */
        public AuditLog.AuditLogBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public AuditLog.AuditLogBuilder admin(final User admin) {
            this.admin = admin;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public AuditLog.AuditLogBuilder action(final String action) {
            this.action = action;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public AuditLog.AuditLogBuilder targetType(final String targetType) {
            this.targetType = targetType;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public AuditLog.AuditLogBuilder targetId(final Long targetId) {
            this.targetId = targetId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public AuditLog.AuditLogBuilder details(final String details) {
            this.details = details;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public AuditLog.AuditLogBuilder createdAt(final LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AuditLog build() {
            return new AuditLog(this.id, this.admin, this.action, this.targetType, this.targetId, this.details, this.createdAt);
        }

        @Override
        public String toString() {
            return "AuditLog.AuditLogBuilder(id=" + this.id + ", admin=" + this.admin + ", action=" + this.action + ", targetType=" + this.targetType + ", targetId=" + this.targetId + ", details=" + this.details + ", createdAt=" + this.createdAt + ")";
        }
    }

    public static AuditLog.AuditLogBuilder builder() {
        return new AuditLog.AuditLogBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public User getAdmin() {
        return this.admin;
    }

    public String getAction() {
        return this.action;
    }

    public String getTargetType() {
        return this.targetType;
    }

    public Long getTargetId() {
        return this.targetId;
    }

    public String getDetails() {
        return this.details;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setAdmin(final User admin) {
        this.admin = admin;
    }

    public void setAction(final String action) {
        this.action = action;
    }

    public void setTargetType(final String targetType) {
        this.targetType = targetType;
    }

    public void setTargetId(final Long targetId) {
        this.targetId = targetId;
    }

    public void setDetails(final String details) {
        this.details = details;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public AuditLog() {
    }

    public AuditLog(final Long id, final User admin, final String action, final String targetType, final Long targetId, final String details, final LocalDateTime createdAt) {
        this.id = id;
        this.admin = admin;
        this.action = action;
        this.targetType = targetType;
        this.targetId = targetId;
        this.details = details;
        this.createdAt = createdAt;
    }
}
