package com.bookseva.report.entity;
import com.bookseva.book.entity.Book;


import com.bookseva.user.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;
    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private TargetType targetType;
    @Column(name = "target_id", nullable = false)
    private Long targetId;
    @Column(nullable = false)
    private String reason;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_by")
    private User resolvedBy;
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    public enum TargetType {
        USER, BOOK;
    }


    public enum ReportStatus {
        PENDING, RESOLVED, DISMISSED;
    }


    public static class ReportBuilder {
        private Long id;
        private User reporter;
        private TargetType targetType;
        private Long targetId;
        private String reason;
        private ReportStatus status;
        private User resolvedBy;
        private LocalDateTime resolvedAt;
        private LocalDateTime createdAt;

        ReportBuilder() {
        }

        /**
         * @return {@code this}.
         */
        public Report.ReportBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Report.ReportBuilder reporter(final User reporter) {
            this.reporter = reporter;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Report.ReportBuilder targetType(final TargetType targetType) {
            this.targetType = targetType;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Report.ReportBuilder targetId(final Long targetId) {
            this.targetId = targetId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Report.ReportBuilder reason(final String reason) {
            this.reason = reason;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Report.ReportBuilder status(final ReportStatus status) {
            this.status = status;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Report.ReportBuilder resolvedBy(final User resolvedBy) {
            this.resolvedBy = resolvedBy;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Report.ReportBuilder resolvedAt(final LocalDateTime resolvedAt) {
            this.resolvedAt = resolvedAt;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Report.ReportBuilder createdAt(final LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Report build() {
            return new Report(this.id, this.reporter, this.targetType, this.targetId, this.reason, this.status, this.resolvedBy, this.resolvedAt, this.createdAt);
        }

        @Override
        public String toString() {
            return "Report.ReportBuilder(id=" + this.id + ", reporter=" + this.reporter + ", targetType=" + this.targetType + ", targetId=" + this.targetId + ", reason=" + this.reason + ", status=" + this.status + ", resolvedBy=" + this.resolvedBy + ", resolvedAt=" + this.resolvedAt + ", createdAt=" + this.createdAt + ")";
        }
    }

    public static Report.ReportBuilder builder() {
        return new Report.ReportBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public User getReporter() {
        return this.reporter;
    }

    public TargetType getTargetType() {
        return this.targetType;
    }

    public Long getTargetId() {
        return this.targetId;
    }

    public String getReason() {
        return this.reason;
    }

    public ReportStatus getStatus() {
        return this.status;
    }

    public User getResolvedBy() {
        return this.resolvedBy;
    }

    public LocalDateTime getResolvedAt() {
        return this.resolvedAt;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setReporter(final User reporter) {
        this.reporter = reporter;
    }

    public void setTargetType(final TargetType targetType) {
        this.targetType = targetType;
    }

    public void setTargetId(final Long targetId) {
        this.targetId = targetId;
    }

    public void setReason(final String reason) {
        this.reason = reason;
    }

    public void setStatus(final ReportStatus status) {
        this.status = status;
    }

    public void setResolvedBy(final User resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    public void setResolvedAt(final LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Report() {
    }

    public Report(final Long id, final User reporter, final TargetType targetType, final Long targetId, final String reason, final ReportStatus status, final User resolvedBy, final LocalDateTime resolvedAt, final LocalDateTime createdAt) {
        this.id = id;
        this.reporter = reporter;
        this.targetType = targetType;
        this.targetId = targetId;
        this.reason = reason;
        this.status = status;
        this.resolvedBy = resolvedBy;
        this.resolvedAt = resolvedAt;
        this.createdAt = createdAt;
    }
}
