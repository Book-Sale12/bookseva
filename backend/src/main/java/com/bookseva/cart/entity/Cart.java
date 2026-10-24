package com.bookseva.cart.entity;

import com.bookseva.user.entity.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CartItem> items;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    private static List<CartItem> $default$items() {
        return new ArrayList<>();
    }


    public static class CartBuilder {
        private Long id;
        private User user;
        private boolean items$set;
        private List<CartItem> items$value;
        private LocalDateTime createdAt;

        CartBuilder() {
        }

        /**
         * @return {@code this}.
         */
        public Cart.CartBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Cart.CartBuilder user(final User user) {
            this.user = user;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Cart.CartBuilder items(final List<CartItem> items) {
            this.items$value = items;
            items$set = true;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Cart.CartBuilder createdAt(final LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Cart build() {
            List<CartItem> items$value = this.items$value;
            if (!this.items$set) items$value = Cart.$default$items();
            return new Cart(this.id, this.user, items$value, this.createdAt);
        }

        @Override
        public String toString() {
            return "Cart.CartBuilder(id=" + this.id + ", user=" + this.user + ", items$value=" + this.items$value + ", createdAt=" + this.createdAt + ")";
        }
    }

    public static Cart.CartBuilder builder() {
        return new Cart.CartBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public User getUser() {
        return this.user;
    }

    public List<CartItem> getItems() {
        return this.items;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public void setItems(final List<CartItem> items) {
        this.items = items;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Cart() {
        this.items = Cart.$default$items();
    }

    public Cart(final Long id, final User user, final List<CartItem> items, final LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.items = items;
        this.createdAt = createdAt;
    }
}
