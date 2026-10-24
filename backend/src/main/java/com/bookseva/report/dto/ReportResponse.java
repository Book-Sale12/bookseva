package com.bookseva.report.dto;

import com.bookseva.report.entity.Report;
import java.time.LocalDateTime;

public class ReportResponse {
    private Long id;
    private Long reporterId;
    private Report.TargetType targetType;
    private Long targetId;
    private String reason;
    private Report.ReportStatus status;
    private LocalDateTime createdAt;

    ReportResponse(final Long id, final Long reporterId, final Report.TargetType targetType, final Long targetId, final String reason, final Report.ReportStatus status, final LocalDateTime createdAt) {
        this.id = id;
        this.reporterId = reporterId;
        this.targetType = targetType;
        this.targetId = targetId;
        this.reason = reason;
        this.status = status;
        this.createdAt = createdAt;
    }


    public static class ReportResponseBuilder {
        private Long id;
        private Long reporterId;
        private Report.TargetType targetType;
        private Long targetId;
        private String reason;
        private Report.ReportStatus status;
        private LocalDateTime createdAt;

        ReportResponseBuilder() {
        }

        /**
         * @return {@code this}.
         */
        public ReportResponse.ReportResponseBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ReportResponse.ReportResponseBuilder reporterId(final Long reporterId) {
            this.reporterId = reporterId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ReportResponse.ReportResponseBuilder targetType(final Report.TargetType targetType) {
            this.targetType = targetType;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ReportResponse.ReportResponseBuilder targetId(final Long targetId) {
            this.targetId = targetId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ReportResponse.ReportResponseBuilder reason(final String reason) {
            this.reason = reason;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ReportResponse.ReportResponseBuilder status(final Report.ReportStatus status) {
            this.status = status;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ReportResponse.ReportResponseBuilder createdAt(final LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ReportResponse build() {
            return new ReportResponse(this.id, this.reporterId, this.targetType, this.targetId, this.reason, this.status, this.createdAt);
        }

        @Override
        public String toString() {
            return "ReportResponse.ReportResponseBuilder(id=" + this.id + ", reporterId=" + this.reporterId + ", targetType=" + this.targetType + ", targetId=" + this.targetId + ", reason=" + this.reason + ", status=" + this.status + ", createdAt=" + this.createdAt + ")";
        }
    }

    public static ReportResponse.ReportResponseBuilder builder() {
        return new ReportResponse.ReportResponseBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public Long getReporterId() {
        return this.reporterId;
    }

    public Report.TargetType getTargetType() {
        return this.targetType;
    }

    public Long getTargetId() {
        return this.targetId;
    }

    public String getReason() {
        return this.reason;
    }

    public Report.ReportStatus getStatus() {
        return this.status;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setReporterId(final Long reporterId) {
        this.reporterId = reporterId;
    }

    public void setTargetType(final Report.TargetType targetType) {
        this.targetType = targetType;
    }

    public void setTargetId(final Long targetId) {
        this.targetId = targetId;
    }

    public void setReason(final String reason) {
        this.reason = reason;
    }

    public void setStatus(final Report.ReportStatus status) {
        this.status = status;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ReportResponse)) return false;
        final ReportResponse other = (ReportResponse) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$reporterId = this.getReporterId();
        final Object other$reporterId = other.getReporterId();
        if (this$reporterId == null ? other$reporterId != null : !this$reporterId.equals(other$reporterId)) return false;
        final Object this$targetId = this.getTargetId();
        final Object other$targetId = other.getTargetId();
        if (this$targetId == null ? other$targetId != null : !this$targetId.equals(other$targetId)) return false;
        final Object this$targetType = this.getTargetType();
        final Object other$targetType = other.getTargetType();
        if (this$targetType == null ? other$targetType != null : !this$targetType.equals(other$targetType)) return false;
        final Object this$reason = this.getReason();
        final Object other$reason = other.getReason();
        if (this$reason == null ? other$reason != null : !this$reason.equals(other$reason)) return false;
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
        final Object this$createdAt = this.getCreatedAt();
        final Object other$createdAt = other.getCreatedAt();
        if (this$createdAt == null ? other$createdAt != null : !this$createdAt.equals(other$createdAt)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ReportResponse;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $reporterId = this.getReporterId();
        result = result * PRIME + ($reporterId == null ? 43 : $reporterId.hashCode());
        final Object $targetId = this.getTargetId();
        result = result * PRIME + ($targetId == null ? 43 : $targetId.hashCode());
        final Object $targetType = this.getTargetType();
        result = result * PRIME + ($targetType == null ? 43 : $targetType.hashCode());
        final Object $reason = this.getReason();
        result = result * PRIME + ($reason == null ? 43 : $reason.hashCode());
        final Object $status = this.getStatus();
        result = result * PRIME + ($status == null ? 43 : $status.hashCode());
        final Object $createdAt = this.getCreatedAt();
        result = result * PRIME + ($createdAt == null ? 43 : $createdAt.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "ReportResponse(id=" + this.getId() + ", reporterId=" + this.getReporterId() + ", targetType=" + this.getTargetType() + ", targetId=" + this.getTargetId() + ", reason=" + this.getReason() + ", status=" + this.getStatus() + ", createdAt=" + this.getCreatedAt() + ")";
    }
}
