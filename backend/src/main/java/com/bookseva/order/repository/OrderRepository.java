package com.bookseva.order.repository;
import com.bookseva.order.entity.OrderStatus;


import com.bookseva.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByBuyerEmailOrderByCreatedAtDesc(String email);
    List<Order> findBySellerEmailOrderByCreatedAtDesc(String email);
    boolean existsByBookIdAndStatusNot(Long bookId, com.bookseva.order.entity.OrderStatus status);
    List<Order> findByStatusAndUpdatedAtBefore(com.bookseva.order.entity.OrderStatus status, java.time.LocalDateTime threshold);
}
