package com.bookseva.dispute.entity;

import com.bookseva.order.entity.Order;
import com.bookseva.user.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "disputes")
public class Dispute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raised_by", nullable = false)
    private User raisedBy;
    @Column(nullable = false)
    private String reason;
    @Column(columnDefinition = "TEXT")
    private String description;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "dispute_evidence_urls", joinColumns = @JoinColumn(name = "dispute_id"))
    @Column(name = "url")
    private List<String> evidenceUrls;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DisputeStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_by")
    private User resolvedBy;
    @Column(name = "resolution_notes", columnDefinition = "TEXT")
    private String resolutionNotes;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;


    public enum DisputeStatus {
        OPEN, RESOLVED_REFUND, RESOLVED_PARTIAL, RESOLVED_DISMISSED;
    }


    public static class DisputeBuilder {
        private Long id;
        private Order order;
        private User raisedBy;
        private String reason;
        private String description;
        private List<String> evidenceUrls;
        private DisputeStatus status;
        private User resolvedBy;
        private String resolutionNotes;
        private LocalDateTime createdAt;
        private LocalDateTime resolvedAt;

        DisputeBuilder() {
        }

        /**
         * @return {@code this}.
         */
        public Dispute.DisputeBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Dispute.DisputeBuilder order(final Order order) {
            this.order = order;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Dispute.DisputeBuilder raisedBy(final User raisedBy) {
            this.raisedBy = raisedBy;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Dispute.DisputeBuilder reason(final String reason) {
            this.reason = reason;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Dispute.DisputeBuilder description(final String description) {
            this.description = description;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Dispute.DisputeBuilder evidenceUrls(final List<String> evidenceUrls) {
            this.evidenceUrls = evidenceUrls;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Dispute.DisputeBuilder status(final DisputeStatus status) {
            this.status = status;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Dispute.DisputeBuilder resolvedBy(final User resolvedBy) {
            this.resolvedBy = resolvedBy;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Dispute.DisputeBuilder resolutionNotes(final String resolutionNotes) {
            this.resolutionNotes = resolutionNotes;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Dispute.DisputeBuilder createdAt(final LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Dispute.DisputeBuilder resolvedAt(final LocalDateTime resolvedAt) {
            this.resolvedAt = resolvedAt;
            return this;
        }

        public Dispute build() {
            return new Dispute(this.id, this.order, this.raisedBy, this.reason, this.description, this.evidenceUrls, this.status, this.resolvedBy, this.resolutionNotes, this.createdAt, this.resolvedAt);
        }

        @Override
        public String toString() {
            return "Dispute.DisputeBuilder(id=" + this.id + ", order=" + this.order + ", raisedBy=" + this.raisedBy + ", reason=" + this.reason + ", description=" + this.description + ", evidenceUrls=" + this.evidenceUrls + ", status=" + this.status + ", resolvedBy=" + this.resolvedBy + ", resolutionNotes=" + this.resolutionNotes + ", createdAt=" + this.createdAt + ", resolvedAt=" + this.resolvedAt + ")";
        }
    }

    public static Dispute.DisputeBuilder builder() {
        return new Dispute.DisputeBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public Order getOrder() {
        return this.order;
    }

    public User getRaisedBy() {
        return this.raisedBy;
    }

    public String getReason() {
        return this.reason;
    }

    public String getDescription() {
        return this.description;
    }

    public List<String> getEvidenceUrls() {
        return this.evidenceUrls;
    }

    public DisputeStatus getStatus() {
        return this.status;
    }

    public User getResolvedBy() {
        return this.resolvedBy;
    }

    public String getResolutionNotes() {
        return this.resolutionNotes;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getResolvedAt() {
        return this.resolvedAt;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public void setRaisedBy(final User raisedBy) {
        this.raisedBy = raisedBy;
    }

    public void setReason(final String reason) {
        this.reason = reason;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setEvidenceUrls(final List<String> evidenceUrls) {
        this.evidenceUrls = evidenceUrls;
    }

    public void setStatus(final DisputeStatus status) {
        this.status = status;
    }

    public void setResolvedBy(final User resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    public void setResolutionNotes(final String resolutionNotes) {
        this.resolutionNotes = resolutionNotes;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setResolvedAt(final LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public Dispute() {
    }

    public Dispute(final Long id, final Order order, final User raisedBy, final String reason, final String description, final List<String> evidenceUrls, final DisputeStatus status, final User resolvedBy, final String resolutionNotes, final LocalDateTime createdAt, final LocalDateTime resolvedAt) {
        this.id = id;
        this.order = order;
        this.raisedBy = raisedBy;
        this.reason = reason;
        this.description = description;
        this.evidenceUrls = evidenceUrls;
        this.status = status;
        this.resolvedBy = resolvedBy;
        this.resolutionNotes = resolutionNotes;
        this.createdAt = createdAt;
        this.resolvedAt = resolvedAt;
    }
}
