package com.bookseva.cart.repository;

import com.bookseva.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteByCartUserIdAndBookId(Long userId, Long bookId);
}
