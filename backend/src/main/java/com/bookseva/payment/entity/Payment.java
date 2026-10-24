package com.bookseva.payment.entity;

import com.bookseva.order.entity.Order;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    @Column(name = "gateway_order_id")
    private String gatewayOrderId;
    @Column(name = "gateway_payment_id")
    private String gatewayPaymentId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false)
    private String method; // e.g. RAZORPAY
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    public static class PaymentBuilder {
        private Long id;
        private Order order;
        private String gatewayOrderId;
        private String gatewayPaymentId;
        private PaymentStatus status;
        private BigDecimal amount;
        private String method;
        private LocalDateTime createdAt;

        PaymentBuilder() {
        }

        /**
         * @return {@code this}.
         */
        public Payment.PaymentBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Payment.PaymentBuilder order(final Order order) {
            this.order = order;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Payment.PaymentBuilder gatewayOrderId(final String gatewayOrderId) {
            this.gatewayOrderId = gatewayOrderId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Payment.PaymentBuilder gatewayPaymentId(final String gatewayPaymentId) {
            this.gatewayPaymentId = gatewayPaymentId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Payment.PaymentBuilder status(final PaymentStatus status) {
            this.status = status;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Payment.PaymentBuilder amount(final BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Payment.PaymentBuilder method(final String method) {
            this.method = method;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Payment.PaymentBuilder createdAt(final LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Payment build() {
            return new Payment(this.id, this.order, this.gatewayOrderId, this.gatewayPaymentId, this.status, this.amount, this.method, this.createdAt);
        }

        @Override
        public String toString() {
            return "Payment.PaymentBuilder(id=" + this.id + ", order=" + this.order + ", gatewayOrderId=" + this.gatewayOrderId + ", gatewayPaymentId=" + this.gatewayPaymentId + ", status=" + this.status + ", amount=" + this.amount + ", method=" + this.method + ", createdAt=" + this.createdAt + ")";
        }
    }

    public static Payment.PaymentBuilder builder() {
        return new Payment.PaymentBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public Order getOrder() {
        return this.order;
    }

    public String getGatewayOrderId() {
        return this.gatewayOrderId;
    }

    public String getGatewayPaymentId() {
        return this.gatewayPaymentId;
    }

    public PaymentStatus getStatus() {
        return this.status;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public String getMethod() {
        return this.method;
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

    public void setGatewayOrderId(final String gatewayOrderId) {
        this.gatewayOrderId = gatewayOrderId;
    }

    public void setGatewayPaymentId(final String gatewayPaymentId) {
        this.gatewayPaymentId = gatewayPaymentId;
    }

    public void setStatus(final PaymentStatus status) {
        this.status = status;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public void setMethod(final String method) {
        this.method = method;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Payment)) return false;
        final Payment other = (Payment) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$order = this.getOrder();
        final Object other$order = other.getOrder();
        if (this$order == null ? other$order != null : !this$order.equals(other$order)) return false;
        final Object this$gatewayOrderId = this.getGatewayOrderId();
        final Object other$gatewayOrderId = other.getGatewayOrderId();
        if (this$gatewayOrderId == null ? other$gatewayOrderId != null : !this$gatewayOrderId.equals(other$gatewayOrderId)) return false;
        final Object this$gatewayPaymentId = this.getGatewayPaymentId();
        final Object other$gatewayPaymentId = other.getGatewayPaymentId();
        if (this$gatewayPaymentId == null ? other$gatewayPaymentId != null : !this$gatewayPaymentId.equals(other$gatewayPaymentId)) return false;
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
        final Object this$amount = this.getAmount();
        final Object other$amount = other.getAmount();
        if (this$amount == null ? other$amount != null : !this$amount.equals(other$amount)) return false;
        final Object this$method = this.getMethod();
        final Object other$method = other.getMethod();
        if (this$method == null ? other$method != null : !this$method.equals(other$method)) return false;
        final Object this$createdAt = this.getCreatedAt();
        final Object other$createdAt = other.getCreatedAt();
        if (this$createdAt == null ? other$createdAt != null : !this$createdAt.equals(other$createdAt)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Payment;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $order = this.getOrder();
        result = result * PRIME + ($order == null ? 43 : $order.hashCode());
        final Object $gatewayOrderId = this.getGatewayOrderId();
        result = result * PRIME + ($gatewayOrderId == null ? 43 : $gatewayOrderId.hashCode());
        final Object $gatewayPaymentId = this.getGatewayPaymentId();
        result = result * PRIME + ($gatewayPaymentId == null ? 43 : $gatewayPaymentId.hashCode());
        final Object $status = this.getStatus();
        result = result * PRIME + ($status == null ? 43 : $status.hashCode());
        final Object $amount = this.getAmount();
        result = result * PRIME + ($amount == null ? 43 : $amount.hashCode());
        final Object $method = this.getMethod();
        result = result * PRIME + ($method == null ? 43 : $method.hashCode());
        final Object $createdAt = this.getCreatedAt();
        result = result * PRIME + ($createdAt == null ? 43 : $createdAt.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Payment(id=" + this.getId() + ", order=" + this.getOrder() + ", gatewayOrderId=" + this.getGatewayOrderId() + ", gatewayPaymentId=" + this.getGatewayPaymentId() + ", status=" + this.getStatus() + ", amount=" + this.getAmount() + ", method=" + this.getMethod() + ", createdAt=" + this.getCreatedAt() + ")";
    }

    public Payment() {
    }

    public Payment(final Long id, final Order order, final String gatewayOrderId, final String gatewayPaymentId, final PaymentStatus status, final BigDecimal amount, final String method, final LocalDateTime createdAt) {
        this.id = id;
        this.order = order;
        this.gatewayOrderId = gatewayOrderId;
        this.gatewayPaymentId = gatewayPaymentId;
        this.status = status;
        this.amount = amount;
        this.method = method;
        this.createdAt = createdAt;
    }
}
