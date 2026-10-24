package com.bookseva.cart.entity;

import com.bookseva.book.entity.Book;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonBackReference
    private Cart cart;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    @CreationTimestamp
    @Column(name = "added_at", updatable = false)
    private LocalDateTime addedAt;


    public static class CartItemBuilder {
        private Long id;
        private Cart cart;
        private Book book;
        private LocalDateTime addedAt;

        CartItemBuilder() {
        }

        /**
         * @return {@code this}.
         */
        public CartItem.CartItemBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public CartItem.CartItemBuilder cart(final Cart cart) {
            this.cart = cart;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public CartItem.CartItemBuilder book(final Book book) {
            this.book = book;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public CartItem.CartItemBuilder addedAt(final LocalDateTime addedAt) {
            this.addedAt = addedAt;
            return this;
        }

        public CartItem build() {
            return new CartItem(this.id, this.cart, this.book, this.addedAt);
        }

        @Override
        public String toString() {
            return "CartItem.CartItemBuilder(id=" + this.id + ", cart=" + this.cart + ", book=" + this.book + ", addedAt=" + this.addedAt + ")";
        }
    }

    public static CartItem.CartItemBuilder builder() {
        return new CartItem.CartItemBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public Cart getCart() {
        return this.cart;
    }

    public Book getBook() {
        return this.book;
    }

    public LocalDateTime getAddedAt() {
        return this.addedAt;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setCart(final Cart cart) {
        this.cart = cart;
    }

    public void setBook(final Book book) {
        this.book = book;
    }

    public void setAddedAt(final LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    public CartItem() {
    }

    public CartItem(final Long id, final Cart cart, final Book book, final LocalDateTime addedAt) {
        this.id = id;
        this.cart = cart;
        this.book = book;
        this.addedAt = addedAt;
    }
}
