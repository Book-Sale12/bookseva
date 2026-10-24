package com.bookseva.dispute.dto;
import com.bookseva.dispute.entity.Dispute;
import com.bookseva.message.entity.Message;


import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class DisputeCreateRequest {
    @NotBlank(message = "Reason is required")
    private String reason;
    private String description;
    private List<String> evidenceUrls;

    public DisputeCreateRequest() {
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

    public void setReason(final String reason) {
        this.reason = reason;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setEvidenceUrls(final List<String> evidenceUrls) {
        this.evidenceUrls = evidenceUrls;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof DisputeCreateRequest)) return false;
        final DisputeCreateRequest other = (DisputeCreateRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$reason = this.getReason();
        final Object other$reason = other.getReason();
        if (this$reason == null ? other$reason != null : !this$reason.equals(other$reason)) return false;
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) return false;
        final Object this$evidenceUrls = this.getEvidenceUrls();
        final Object other$evidenceUrls = other.getEvidenceUrls();
        if (this$evidenceUrls == null ? other$evidenceUrls != null : !this$evidenceUrls.equals(other$evidenceUrls)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof DisputeCreateRequest;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $reason = this.getReason();
        result = result * PRIME + ($reason == null ? 43 : $reason.hashCode());
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        final Object $evidenceUrls = this.getEvidenceUrls();
        result = result * PRIME + ($evidenceUrls == null ? 43 : $evidenceUrls.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "DisputeCreateRequest(reason=" + this.getReason() + ", description=" + this.getDescription() + ", evidenceUrls=" + this.getEvidenceUrls() + ")";
    }
}
