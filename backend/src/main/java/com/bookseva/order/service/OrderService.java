package com.bookseva.order.service;

import com.bookseva.book.Book;
import com.bookseva.book.BookRepository;
import com.bookseva.book.ListingStatus;
import com.bookseva.cart.CartItem;
import com.bookseva.cart.repository.CartItemRepository;
import com.bookseva.notification.service.MailService;
import com.bookseva.notification.service.NotificationService;
import com.bookseva.order.Order;
import com.bookseva.order.OrderStatus;
import com.bookseva.order.repository.OrderRepository;
import com.bookseva.payment.Payment;
import com.bookseva.payment.PaymentStatus;
import com.bookseva.payment.repository.PaymentRepository;
import com.bookseva.invoice.service.InvoiceService;
import com.bookseva.user.User;
import com.bookseva.user.UserRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final RazorpayClient razorpayClient;
    private final InvoiceService invoiceService;
    private final NotificationService notificationService;
    private final MailService mailService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public List<Payment> checkout(List<Long> cartItemIds, String userEmail) throws RazorpayException {
        User buyer = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));

        if (cartItemIds == null || cartItemIds.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        List<CartItem> validCartItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Long itemId : cartItemIds) {
            CartItem cartItem = cartItemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Cart item not found"));

            if (!cartItem.getCart().getUser().getId().equals(buyer.getId())) {
                throw new RuntimeException("Unauthorized");
            }

            Book book = bookRepository.findByIdWithLock(cartItem.getBook().getId())
                    .orElseThrow(() -> new RuntimeException("Book not found"));
            if (book.getStatus() != ListingStatus.ACTIVE || book.getQuantity() < 1) {
                throw new RuntimeException("Book " + book.getTitle() + " is no longer available");
            }

            validCartItems.add(cartItem);
            totalAmount = totalAmount.add(book.getPrice());
        }

        // Create a single Razorpay Order for the total amount
        JSONObject orderRequest = new JSONObject();
        // Amount in paise
        orderRequest.put("amount", totalAmount.multiply(new BigDecimal("100")).setScale(0, java.math.RoundingMode.HALF_UP).longValue());
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "txn_cart_" + System.currentTimeMillis());

        com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequest);
        String gatewayOrderId = razorpayOrder.get("id");

        List<Payment> payments = new ArrayList<>();

        for (CartItem cartItem : validCartItems) {
            Book book = cartItem.getBook();

            Order order = Order.builder()
                    .buyer(buyer)
                    .seller(book.getSeller())
                    .book(book)
                    .status(OrderStatus.PAYMENT_PENDING)
                    .amount(book.getPrice())
                    .platformFee(BigDecimal.ZERO)
                    .build();

            orderRepository.save(order);

            Payment payment = Payment.builder()
                    .order(order)
                    .gatewayOrderId(gatewayOrderId)
                    .amount(book.getPrice())
                    .method("RAZORPAY")
                    .status(PaymentStatus.PENDING)
                    .build();

            paymentRepository.save(payment);
            payments.add(payment);
        }

        return payments;
    }

    @Transactional
    public void cancelOrder(Long orderId, String userEmail) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getBuyer().getEmail().equals(userEmail) && !order.getSeller().getEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized");
        }

        if (order.getStatus() == OrderStatus.PAYMENT_PENDING) {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Can only cancel pending orders");
        }
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersForBuyer(String email) {
        return orderRepository.findByBuyerEmailOrderByCreatedAtDesc(email);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersForSeller(String email) {
        return orderRepository.findBySellerEmailOrderByCreatedAtDesc(email);
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Long id, String email) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if (!order.getBuyer().getEmail().equals(email) && !order.getSeller().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }
        return order;
    }

    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus newStatus, String userEmail) {
        Order order = getOrderById(orderId, userEmail);

        boolean isSeller = order.getSeller().getEmail().equals(userEmail);
        boolean isBuyer = order.getBuyer().getEmail().equals(userEmail);

        if (newStatus == OrderStatus.CONFIRMED_BY_SELLER) {
            if (!isSeller) throw new RuntimeException("Only seller can confirm order");
            if (order.getStatus() != OrderStatus.PAID) throw new RuntimeException("Invalid status transition");
            order.setStatus(newStatus);
        } else if (newStatus == OrderStatus.HANDED_OVER) {
            if (!isSeller) throw new RuntimeException("Only seller can mark as handed over");
            if (order.getStatus() != OrderStatus.CONFIRMED_BY_SELLER) throw new RuntimeException("Invalid status transition");
            order.setStatus(newStatus);
            
            // Notifications to buyer
            notificationService.sendNotification(
                order.getBuyer(),
                "ORDER_HANDED_OVER",
                "Order Handed Over",
                "The seller has handed over '" + order.getBook().getTitle() + "'. Please confirm receipt once you have it.",
                "/orders"
            );
            mailService.sendEmail(
                order.getBuyer().getEmail(),
                "Order Handed Over - " + order.getBook().getTitle(),
                "The seller has marked your order for '" + order.getBook().getTitle() + "' as handed over. Please log in and confirm receipt once you receive it."
            );
        } else if (newStatus == OrderStatus.COMPLETED) {
            if (!isBuyer) throw new RuntimeException("Only buyer can confirm receipt");
            if (order.getStatus() != OrderStatus.HANDED_OVER) throw new RuntimeException("Invalid status transition");
            order.setStatus(newStatus);
            
            // Trigger invoice generation async
            java.util.concurrent.CompletableFuture.runAsync(() -> {
                invoiceService.generateInvoice(order.getId());
            });
        } else {
            throw new RuntimeException("Unsupported status transition");
        }

        orderRepository.save(order);
    }
}
