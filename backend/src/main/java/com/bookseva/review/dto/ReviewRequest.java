package com.bookseva.review.dto;
import com.bookseva.review.entity.Review;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ReviewRequest {
    @NotNull
    private Long orderId;
    @NotNull
    private Long revieweeId;
    @Min(1)
    @Max(5)
    private int rating;
    private String comment;

    public ReviewRequest() {
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public Long getRevieweeId() {
        return this.revieweeId;
    }

    public int getRating() {
        return this.rating;
    }

    public String getComment() {
        return this.comment;
    }

    public void setOrderId(final Long orderId) {
        this.orderId = orderId;
    }

    public void setRevieweeId(final Long revieweeId) {
        this.revieweeId = revieweeId;
    }

    public void setRating(final int rating) {
        this.rating = rating;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ReviewRequest)) return false;
        final ReviewRequest other = (ReviewRequest) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getRating() != other.getRating()) return false;
        final Object this$orderId = this.getOrderId();
        final Object other$orderId = other.getOrderId();
        if (this$orderId == null ? other$orderId != null : !this$orderId.equals(other$orderId)) return false;
        final Object this$revieweeId = this.getRevieweeId();
        final Object other$revieweeId = other.getRevieweeId();
        if (this$revieweeId == null ? other$revieweeId != null : !this$revieweeId.equals(other$revieweeId)) return false;
        final Object this$comment = this.getComment();
        final Object other$comment = other.getComment();
        if (this$comment == null ? other$comment != null : !this$comment.equals(other$comment)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ReviewRequest;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getRating();
        final Object $orderId = this.getOrderId();
        result = result * PRIME + ($orderId == null ? 43 : $orderId.hashCode());
        final Object $revieweeId = this.getRevieweeId();
        result = result * PRIME + ($revieweeId == null ? 43 : $revieweeId.hashCode());
        final Object $comment = this.getComment();
        result = result * PRIME + ($comment == null ? 43 : $comment.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "ReviewRequest(orderId=" + this.getOrderId() + ", revieweeId=" + this.getRevieweeId() + ", rating=" + this.getRating() + ", comment=" + this.getComment() + ")";
    }
}
