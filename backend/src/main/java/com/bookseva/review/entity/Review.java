package com.bookseva.review.entity;

import com.bookseva.order.entity.Order;
import com.bookseva.user.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewee_id", nullable = false)
    private User reviewee;
    @Column(nullable = false)
    private int rating; // 1 to 5
    @Column(columnDefinition = "TEXT")
    private String comment;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    public static class ReviewBuilder {
        private Long id;
        private Order order;
        private User reviewer;
        private User reviewee;
        private int rating;
        private String comment;
        private LocalDateTime createdAt;

        ReviewBuilder() {
        }

        /**
         * @return {@code this}.
         */
        public Review.ReviewBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Review.ReviewBuilder order(final Order order) {
            this.order = order;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Review.ReviewBuilder reviewer(final User reviewer) {
            this.reviewer = reviewer;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Review.ReviewBuilder reviewee(final User reviewee) {
            this.reviewee = reviewee;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Review.ReviewBuilder rating(final int rating) {
            this.rating = rating;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Review.ReviewBuilder comment(final String comment) {
            this.comment = comment;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Review.ReviewBuilder createdAt(final LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Review build() {
            return new Review(this.id, this.order, this.reviewer, this.reviewee, this.rating, this.comment, this.createdAt);
        }

        @Override
        public String toString() {
            return "Review.ReviewBuilder(id=" + this.id + ", order=" + this.order + ", reviewer=" + this.reviewer + ", reviewee=" + this.reviewee + ", rating=" + this.rating + ", comment=" + this.comment + ", createdAt=" + this.createdAt + ")";
        }
    }

    public static Review.ReviewBuilder builder() {
        return new Review.ReviewBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public Order getOrder() {
        return this.order;
    }

    public User getReviewer() {
        return this.reviewer;
    }

    public User getReviewee() {
        return this.reviewee;
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

    public void setOrder(final Order order) {
        this.order = order;
    }

    public void setReviewer(final User reviewer) {
        this.reviewer = reviewer;
    }

    public void setReviewee(final User reviewee) {
        this.reviewee = reviewee;
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

    public Review() {
    }

    public Review(final Long id, final Order order, final User reviewer, final User reviewee, final int rating, final String comment, final LocalDateTime createdAt) {
        this.id = id;
        this.order = order;
        this.reviewer = reviewer;
        this.reviewee = reviewee;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }
}
