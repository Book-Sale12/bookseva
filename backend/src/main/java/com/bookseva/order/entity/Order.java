package com.bookseva.order.entity;

import com.bookseva.book.entity.Book;
import com.bookseva.user.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(name = "platform_fee", nullable = false)
    private BigDecimal platformFee;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    @org.hibernate.annotations.UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    public static class OrderBuilder {
        private Long id;
        private User buyer;
        private User seller;
        private Book book;
        private OrderStatus status;
        private BigDecimal amount;
        private BigDecimal platformFee;
        private LocalDateTime createdAt;
        private LocalDateTime completedAt;
        private LocalDateTime updatedAt;

        OrderBuilder() {
        }

        /**
         * @return {@code this}.
         */
        public Order.OrderBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Order.OrderBuilder buyer(final User buyer) {
            this.buyer = buyer;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Order.OrderBuilder seller(final User seller) {
            this.seller = seller;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Order.OrderBuilder book(final Book book) {
            this.book = book;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Order.OrderBuilder status(final OrderStatus status) {
            this.status = status;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Order.OrderBuilder amount(final BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Order.OrderBuilder platformFee(final BigDecimal platformFee) {
            this.platformFee = platformFee;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Order.OrderBuilder createdAt(final LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Order.OrderBuilder completedAt(final LocalDateTime completedAt) {
            this.completedAt = completedAt;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Order.OrderBuilder updatedAt(final LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Order build() {
            return new Order(this.id, this.buyer, this.seller, this.book, this.status, this.amount, this.platformFee, this.createdAt, this.completedAt, this.updatedAt);
        }

        @Override
        public String toString() {
            return "Order.OrderBuilder(id=" + this.id + ", buyer=" + this.buyer + ", seller=" + this.seller + ", book=" + this.book + ", status=" + this.status + ", amount=" + this.amount + ", platformFee=" + this.platformFee + ", createdAt=" + this.createdAt + ", completedAt=" + this.completedAt + ", updatedAt=" + this.updatedAt + ")";
        }
    }

    public static Order.OrderBuilder builder() {
        return new Order.OrderBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public User getBuyer() {
        return this.buyer;
    }

    public User getSeller() {
        return this.seller;
    }

    public Book getBook() {
        return this.book;
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public BigDecimal getPlatformFee() {
        return this.platformFee;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return this.completedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setBuyer(final User buyer) {
        this.buyer = buyer;
    }

    public void setSeller(final User seller) {
        this.seller = seller;
    }

    public void setBook(final Book book) {
        this.book = book;
    }

    public void setStatus(final OrderStatus status) {
        this.status = status;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public void setPlatformFee(final BigDecimal platformFee) {
        this.platformFee = platformFee;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setCompletedAt(final LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public void setUpdatedAt(final LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Order)) return false;
        final Order other = (Order) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$buyer = this.getBuyer();
        final Object other$buyer = other.getBuyer();
        if (this$buyer == null ? other$buyer != null : !this$buyer.equals(other$buyer)) return false;
        final Object this$seller = this.getSeller();
        final Object other$seller = other.getSeller();
        if (this$seller == null ? other$seller != null : !this$seller.equals(other$seller)) return false;
        final Object this$book = this.getBook();
        final Object other$book = other.getBook();
        if (this$book == null ? other$book != null : !this$book.equals(other$book)) return false;
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
        final Object this$amount = this.getAmount();
        final Object other$amount = other.getAmount();
        if (this$amount == null ? other$amount != null : !this$amount.equals(other$amount)) return false;
        final Object this$platformFee = this.getPlatformFee();
        final Object other$platformFee = other.getPlatformFee();
        if (this$platformFee == null ? other$platformFee != null : !this$platformFee.equals(other$platformFee)) return false;
        final Object this$createdAt = this.getCreatedAt();
        final Object other$createdAt = other.getCreatedAt();
        if (this$createdAt == null ? other$createdAt != null : !this$createdAt.equals(other$createdAt)) return false;
        final Object this$completedAt = this.getCompletedAt();
        final Object other$completedAt = other.getCompletedAt();
        if (this$completedAt == null ? other$completedAt != null : !this$completedAt.equals(other$completedAt)) return false;
        final Object this$updatedAt = this.getUpdatedAt();
        final Object other$updatedAt = other.getUpdatedAt();
        if (this$updatedAt == null ? other$updatedAt != null : !this$updatedAt.equals(other$updatedAt)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Order;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $buyer = this.getBuyer();
        result = result * PRIME + ($buyer == null ? 43 : $buyer.hashCode());
        final Object $seller = this.getSeller();
        result = result * PRIME + ($seller == null ? 43 : $seller.hashCode());
        final Object $book = this.getBook();
        result = result * PRIME + ($book == null ? 43 : $book.hashCode());
        final Object $status = this.getStatus();
        result = result * PRIME + ($status == null ? 43 : $status.hashCode());
        final Object $amount = this.getAmount();
        result = result * PRIME + ($amount == null ? 43 : $amount.hashCode());
        final Object $platformFee = this.getPlatformFee();
        result = result * PRIME + ($platformFee == null ? 43 : $platformFee.hashCode());
        final Object $createdAt = this.getCreatedAt();
        result = result * PRIME + ($createdAt == null ? 43 : $createdAt.hashCode());
        final Object $completedAt = this.getCompletedAt();
        result = result * PRIME + ($completedAt == null ? 43 : $completedAt.hashCode());
        final Object $updatedAt = this.getUpdatedAt();
        result = result * PRIME + ($updatedAt == null ? 43 : $updatedAt.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Order(id=" + this.getId() + ", buyer=" + this.getBuyer() + ", seller=" + this.getSeller() + ", book=" + this.getBook() + ", status=" + this.getStatus() + ", amount=" + this.getAmount() + ", platformFee=" + this.getPlatformFee() + ", createdAt=" + this.getCreatedAt() + ", completedAt=" + this.getCompletedAt() + ", updatedAt=" + this.getUpdatedAt() + ")";
    }

    public Order() {
    }

    public Order(final Long id, final User buyer, final User seller, final Book book, final OrderStatus status, final BigDecimal amount, final BigDecimal platformFee, final LocalDateTime createdAt, final LocalDateTime completedAt, final LocalDateTime updatedAt) {
        this.id = id;
        this.buyer = buyer;
        this.seller = seller;
        this.book = book;
        this.status = status;
        this.amount = amount;
        this.platformFee = platformFee;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
        this.updatedAt = updatedAt;
    }
}
