package com.bookseva.dispute.dto;
import com.bookseva.dispute.entity.Dispute;
import com.bookseva.message.entity.Message;


import com.bookseva.dispute.entity.Dispute.DisputeStatus;
import jakarta.validation.constraints.NotNull;

public class DisputeResolveRequest {
    @NotNull(message = "New status is required")
    private DisputeStatus newStatus;
    private String resolutionNotes;

    public DisputeResolveRequest() {
    }

    public DisputeStatus getNewStatus() {
        return this.newStatus;
    }

    public String getResolutionNotes() {
        return this.resolutionNotes;
    }

    public void setNewStatus(final DisputeStatus newStatus) {
        this.newStatus = newStatus;
    }

    public void setResolutionNotes(final String resolutionNotes) {
        this.resolutionNotes = resolutionNotes;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof DisputeResolveRequest)) return false;
        final DisputeResolveRequest other = (DisputeResolveRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$newStatus = this.getNewStatus();
        final Object other$newStatus = other.getNewStatus();
        if (this$newStatus == null ? other$newStatus != null : !this$newStatus.equals(other$newStatus)) return false;
        final Object this$resolutionNotes = this.getResolutionNotes();
        final Object other$resolutionNotes = other.getResolutionNotes();
        if (this$resolutionNotes == null ? other$resolutionNotes != null : !this$resolutionNotes.equals(other$resolutionNotes)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof DisputeResolveRequest;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $newStatus = this.getNewStatus();
        result = result * PRIME + ($newStatus == null ? 43 : $newStatus.hashCode());
        final Object $resolutionNotes = this.getResolutionNotes();
        result = result * PRIME + ($resolutionNotes == null ? 43 : $resolutionNotes.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "DisputeResolveRequest(newStatus=" + this.getNewStatus() + ", resolutionNotes=" + this.getResolutionNotes() + ")";
    }
}
