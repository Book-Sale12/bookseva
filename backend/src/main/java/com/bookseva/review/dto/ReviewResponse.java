package com.bookseva.review.dto;
import com.bookseva.review.entity.Review;


import java.time.LocalDateTime;

public class ReviewResponse {
    private Long id;
    private Long orderId;
    private Long reviewerId;
    private String reviewerName;
    private Long revieweeId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;

    ReviewResponse(final Long id, final Long orderId, final Long reviewerId, final String reviewerName, final Long revieweeId, final int rating, final String comment, final LocalDateTime createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.reviewerId = reviewerId;
        this.reviewerName = reviewerName;
        this.revieweeId = revieweeId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }


    public static class ReviewResponseBuilder {
        private Long id;
        private Long orderId;
        private Long reviewerId;
        private String reviewerName;
        private Long revieweeId;
        private int rating;
        private String comment;
        private LocalDateTime createdAt;

        ReviewResponseBuilder() {
        }

        /**
         * @return {@code this}.
         */
        public ReviewResponse.ReviewResponseBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ReviewResponse.ReviewResponseBuilder orderId(final Long orderId) {
            this.orderId = orderId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ReviewResponse.ReviewResponseBuilder reviewerId(final Long reviewerId) {
            this.reviewerId = reviewerId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ReviewResponse.ReviewResponseBuilder reviewerName(final String reviewerName) {
            this.reviewerName = reviewerName;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ReviewResponse.ReviewResponseBuilder revieweeId(final Long revieweeId) {
            this.revieweeId = revieweeId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ReviewResponse.ReviewResponseBuilder rating(final int rating) {
            this.rating = rating;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ReviewResponse.ReviewResponseBuilder comment(final String comment) {
            this.comment = comment;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ReviewResponse.ReviewResponseBuilder createdAt(final LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ReviewResponse build() {
            return new ReviewResponse(this.id, this.orderId, this.reviewerId, this.reviewerName, this.revieweeId, this.rating, this.comment, this.createdAt);
        }

        @Override
        public String toString() {
            return "ReviewResponse.ReviewResponseBuilder(id=" + this.id + ", orderId=" + this.orderId + ", reviewerId=" + this.reviewerId + ", reviewerName=" + this.reviewerName + ", revieweeId=" + this.revieweeId + ", rating=" + this.rating + ", comment=" + this.comment + ", createdAt=" + this.createdAt + ")";
        }
    }

    public static ReviewResponse.ReviewResponseBuilder builder() {
        return new ReviewResponse.ReviewResponseBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public Long getReviewerId() {
        return this.reviewerId;
    }

    public String getReviewerName() {
        return this.reviewerName;
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

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setOrderId(final Long orderId) {
        this.orderId = orderId;
    }

    public void setReviewerId(final Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public void setReviewerName(final String reviewerName) {
        this.reviewerName = reviewerName;
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

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ReviewResponse)) return false;
        final ReviewResponse other = (ReviewResponse) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getRating() != other.getRating()) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$orderId = this.getOrderId();
        final Object other$orderId = other.getOrderId();
        if (this$orderId == null ? other$orderId != null : !this$orderId.equals(other$orderId)) return false;
        final Object this$reviewerId = this.getReviewerId();
        final Object other$reviewerId = other.getReviewerId();
        if (this$reviewerId == null ? other$reviewerId != null : !this$reviewerId.equals(other$reviewerId)) return false;
        final Object this$revieweeId = this.getRevieweeId();
        final Object other$revieweeId = other.getRevieweeId();
        if (this$revieweeId == null ? other$revieweeId != null : !this$revieweeId.equals(other$revieweeId)) return false;
        final Object this$reviewerName = this.getReviewerName();
        final Object other$reviewerName = other.getReviewerName();
        if (this$reviewerName == null ? other$reviewerName != null : !this$reviewerName.equals(other$reviewerName)) return false;
        final Object this$comment = this.getComment();
        final Object other$comment = other.getComment();
        if (this$comment == null ? other$comment != null : !this$comment.equals(other$comment)) return false;
        final Object this$createdAt = this.getCreatedAt();
        final Object other$createdAt = other.getCreatedAt();
        if (this$createdAt == null ? other$createdAt != null : !this$createdAt.equals(other$createdAt)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ReviewResponse;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getRating();
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $orderId = this.getOrderId();
        result = result * PRIME + ($orderId == null ? 43 : $orderId.hashCode());
        final Object $reviewerId = this.getReviewerId();
        result = result * PRIME + ($reviewerId == null ? 43 : $reviewerId.hashCode());
        final Object $revieweeId = this.getRevieweeId();
        result = result * PRIME + ($revieweeId == null ? 43 : $revieweeId.hashCode());
        final Object $reviewerName = this.getReviewerName();
        result = result * PRIME + ($reviewerName == null ? 43 : $reviewerName.hashCode());
        final Object $comment = this.getComment();
        result = result * PRIME + ($comment == null ? 43 : $comment.hashCode());
        final Object $createdAt = this.getCreatedAt();
        result = result * PRIME + ($createdAt == null ? 43 : $createdAt.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "ReviewResponse(id=" + this.getId() + ", orderId=" + this.getOrderId() + ", reviewerId=" + this.getReviewerId() + ", reviewerName=" + this.getReviewerName() + ", revieweeId=" + this.getRevieweeId() + ", rating=" + this.getRating() + ", comment=" + this.getComment() + ", createdAt=" + this.getCreatedAt() + ")";
    }
}
