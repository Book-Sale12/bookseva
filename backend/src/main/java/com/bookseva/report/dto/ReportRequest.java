package com.bookseva.report.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.bookseva.report.entity.Report;

public class ReportRequest {
    @NotNull
    private Report.TargetType targetType;
    @NotNull
    private Long targetId;
    @NotBlank
    private String reason;

    public ReportRequest() {
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

    public void setTargetType(final Report.TargetType targetType) {
        this.targetType = targetType;
    }

    public void setTargetId(final Long targetId) {
        this.targetId = targetId;
    }

    public void setReason(final String reason) {
        this.reason = reason;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ReportRequest)) return false;
        final ReportRequest other = (ReportRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$targetId = this.getTargetId();
        final Object other$targetId = other.getTargetId();
        if (this$targetId == null ? other$targetId != null : !this$targetId.equals(other$targetId)) return false;
        final Object this$targetType = this.getTargetType();
        final Object other$targetType = other.getTargetType();
        if (this$targetType == null ? other$targetType != null : !this$targetType.equals(other$targetType)) return false;
        final Object this$reason = this.getReason();
        final Object other$reason = other.getReason();
        if (this$reason == null ? other$reason != null : !this$reason.equals(other$reason)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ReportRequest;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $targetId = this.getTargetId();
        result = result * PRIME + ($targetId == null ? 43 : $targetId.hashCode());
        final Object $targetType = this.getTargetType();
        result = result * PRIME + ($targetType == null ? 43 : $targetType.hashCode());
        final Object $reason = this.getReason();
        result = result * PRIME + ($reason == null ? 43 : $reason.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "ReportRequest(targetType=" + this.getTargetType() + ", targetId=" + this.getTargetId() + ", reason=" + this.getReason() + ")";
    }
}
