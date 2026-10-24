package com.bookseva.invoice.entity;

import com.bookseva.order.entity.Order;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;
    @Column(name = "file_url", nullable = false)
    private String fileUrl;
    @CreationTimestamp
    @Column(name = "generated_at", updatable = false)
    private LocalDateTime generatedAt;


    public static class InvoiceBuilder {
        private Long id;
        private Order order;
        private String fileUrl;
        private LocalDateTime generatedAt;

        InvoiceBuilder() {
        }

        /**
         * @return {@code this}.
         */
        public Invoice.InvoiceBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Invoice.InvoiceBuilder order(final Order order) {
            this.order = order;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Invoice.InvoiceBuilder fileUrl(final String fileUrl) {
            this.fileUrl = fileUrl;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Invoice.InvoiceBuilder generatedAt(final LocalDateTime generatedAt) {
            this.generatedAt = generatedAt;
            return this;
        }

        public Invoice build() {
            return new Invoice(this.id, this.order, this.fileUrl, this.generatedAt);
        }

        @Override
        public String toString() {
            return "Invoice.InvoiceBuilder(id=" + this.id + ", order=" + this.order + ", fileUrl=" + this.fileUrl + ", generatedAt=" + this.generatedAt + ")";
        }
    }

    public static Invoice.InvoiceBuilder builder() {
        return new Invoice.InvoiceBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public Order getOrder() {
        return this.order;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public LocalDateTime getGeneratedAt() {
        return this.generatedAt;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public void setFileUrl(final String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setGeneratedAt(final LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Invoice)) return false;
        final Invoice other = (Invoice) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$order = this.getOrder();
        final Object other$order = other.getOrder();
        if (this$order == null ? other$order != null : !this$order.equals(other$order)) return false;
        final Object this$fileUrl = this.getFileUrl();
        final Object other$fileUrl = other.getFileUrl();
        if (this$fileUrl == null ? other$fileUrl != null : !this$fileUrl.equals(other$fileUrl)) return false;
        final Object this$generatedAt = this.getGeneratedAt();
        final Object other$generatedAt = other.getGeneratedAt();
        if (this$generatedAt == null ? other$generatedAt != null : !this$generatedAt.equals(other$generatedAt)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Invoice;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $order = this.getOrder();
        result = result * PRIME + ($order == null ? 43 : $order.hashCode());
        final Object $fileUrl = this.getFileUrl();
        result = result * PRIME + ($fileUrl == null ? 43 : $fileUrl.hashCode());
        final Object $generatedAt = this.getGeneratedAt();
        result = result * PRIME + ($generatedAt == null ? 43 : $generatedAt.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Invoice(id=" + this.getId() + ", order=" + this.getOrder() + ", fileUrl=" + this.getFileUrl() + ", generatedAt=" + this.getGeneratedAt() + ")";
    }

    public Invoice() {
    }

    public Invoice(final Long id, final Order order, final String fileUrl, final LocalDateTime generatedAt) {
        this.id = id;
        this.order = order;
        this.fileUrl = fileUrl;
        this.generatedAt = generatedAt;
    }
}
