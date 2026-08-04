package com.bookseva.order.job;

import com.bookseva.order.Order;
import com.bookseva.order.OrderStatus;
import com.bookseva.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderAutoConfirmJob {
    private final OrderRepository orderRepository;
    private final com.bookseva.invoice.service.InvoiceService invoiceService;

    @Scheduled(cron = "0 0 * * * *") // Run every hour
    @Transactional
    public void autoConfirmOrders() {
        log.info("Running auto-confirm orders job...");
        LocalDateTime threshold = LocalDateTime.now().minusHours(48);
        
        List<Order> eligibleOrders = orderRepository.findAll().stream()
                .filter(o -> o.getStatus() == OrderStatus.HANDED_OVER)
                .filter(o -> o.getUpdatedAt() != null && o.getUpdatedAt().isBefore(threshold))
                .toList();

        for (Order order : eligibleOrders) {
            log.info("Auto-confirming order {}", order.getId());
            order.setStatus(OrderStatus.COMPLETED);
            orderRepository.save(order);
            invoiceService.generateInvoice(order.getId());
        }
    }
}
