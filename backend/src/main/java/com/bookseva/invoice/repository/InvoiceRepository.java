package com.bookseva.invoice.repository;

import com.bookseva.invoice.Invoice;
import com.bookseva.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByOrder(Order order);
    Optional<Invoice> findByOrderId(Long orderId);
}
