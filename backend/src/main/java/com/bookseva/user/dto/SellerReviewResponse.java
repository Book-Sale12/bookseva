package com.bookseva.user.dto;
import com.bookseva.user.entity.User;


import java.time.LocalDateTime;

public class SellerReviewResponse {
    private Long id;
    private String reviewerName;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;

    SellerReviewResponse(final Long id, final String reviewerName, final int rating, final String comment, final LocalDateTime createdAt) {
        this.id = id;
        this.reviewerName = reviewerName;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }


    public static class SellerReviewResponseBuilder {
        private Long id;
        private String reviewerName;
        private int rating;
        private String comment;
        private LocalDateTime createdAt;

        SellerReviewResponseBuilder() {
        }

        /**
         * @return {@code this}.
         */
        public SellerReviewResponse.SellerReviewResponseBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public SellerReviewResponse.SellerReviewResponseBuilder reviewerName(final String reviewerName) {
            this.reviewerName = reviewerName;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public SellerReviewResponse.SellerReviewResponseBuilder rating(final int rating) {
            this.rating = rating;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public SellerReviewResponse.SellerReviewResponseBuilder comment(final String comment) {
            this.comment = comment;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public SellerReviewResponse.SellerReviewResponseBuilder createdAt(final LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public SellerReviewResponse build() {
            return new SellerReviewResponse(this.id, this.reviewerName, this.rating, this.comment, this.createdAt);
        }

        @Override
        public String toString() {
            return "SellerReviewResponse.SellerReviewResponseBuilder(id=" + this.id + ", reviewerName=" + this.reviewerName + ", rating=" + this.rating + ", comment=" + this.comment + ", createdAt=" + this.createdAt + ")";
        }
    }

    public static SellerReviewResponse.SellerReviewResponseBuilder builder() {
        return new SellerReviewResponse.SellerReviewResponseBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public String getReviewerName() {
        return this.reviewerName;
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

    public void setReviewerName(final String reviewerName) {
        this.reviewerName = reviewerName;
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
        if (!(o instanceof SellerReviewResponse)) return false;
        final SellerReviewResponse other = (SellerReviewResponse) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getRating() != other.getRating()) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
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
        return other instanceof SellerReviewResponse;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getRating();
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
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
        return "SellerReviewResponse(id=" + this.getId() + ", reviewerName=" + this.getReviewerName() + ", rating=" + this.getRating() + ", comment=" + this.getComment() + ", createdAt=" + this.getCreatedAt() + ")";
    }
}
