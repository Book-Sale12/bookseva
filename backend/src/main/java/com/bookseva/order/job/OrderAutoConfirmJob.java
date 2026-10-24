package com.bookseva.order.job;
import com.bookseva.invoice.entity.Invoice;


import com.bookseva.order.entity.Order;
import com.bookseva.order.entity.OrderStatus;
import com.bookseva.order.repository.OrderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderAutoConfirmJob {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OrderAutoConfirmJob.class);
    private final OrderRepository orderRepository;
    private final com.bookseva.invoice.service.InvoiceService invoiceService;

    // Run every hour
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void autoConfirmOrders() {
        log.info("Running auto-confirm orders job...");
        LocalDateTime threshold = LocalDateTime.now().minusHours(48);
        List<Order> eligibleOrders = orderRepository.findByStatusAndUpdatedAtBefore(OrderStatus.HANDED_OVER, threshold);
        for (Order order : eligibleOrders) {
            log.info("Auto-confirming order {}", order.getId());
            order.setStatus(OrderStatus.COMPLETED);
            orderRepository.save(order);
            invoiceService.generateInvoice(order.getId());
        }
    }

    public OrderAutoConfirmJob(final OrderRepository orderRepository, final com.bookseva.invoice.service.InvoiceService invoiceService) {
        this.orderRepository = orderRepository;
        this.invoiceService = invoiceService;
    }
}
