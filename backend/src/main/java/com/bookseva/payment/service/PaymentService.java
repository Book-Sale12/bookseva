package com.bookseva.payment.service;

import com.bookseva.order.Order;
import com.bookseva.order.OrderStatus;
import com.bookseva.order.repository.OrderRepository;
import com.bookseva.payment.Payment;
import com.bookseva.payment.PaymentStatus;
import com.bookseva.payment.repository.PaymentRepository;
import com.bookseva.cart.repository.CartItemRepository;
import com.bookseva.invoice.service.InvoiceService;
import com.bookseva.book.Book;
import com.bookseva.book.BookRepository;
import com.bookseva.book.ListingStatus;
import com.bookseva.notification.service.MailService;
import com.bookseva.notification.service.NotificationService;
import com.bookseva.payment.dto.VerifyPaymentRequest;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final InvoiceService invoiceService;
    private final BookRepository bookRepository;
    private final NotificationService notificationService;
    private final MailService mailService;

    @Value("${razorpay.key-secret}")
    private String razorpaySecret;

    @Transactional
    public void handleWebhook(String payload, String signature) {
        try {
            boolean isValid = Utils.verifyWebhookSignature(payload, signature, razorpaySecret);
            if (!isValid) {
                throw new RuntimeException("Invalid Razorpay webhook signature");
            }

            JSONObject json = new JSONObject(payload);
            String event = json.getString("event");
            
            if ("payment.captured".equals(event)) {
                JSONObject paymentEntity = json.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity");
                String gatewayOrderId = paymentEntity.getString("order_id");
                String gatewayPaymentId = paymentEntity.getString("id");

                Payment payment = paymentRepository.findByGatewayOrderId(gatewayOrderId)
                        .orElseThrow(() -> new RuntimeException("Payment not found for gateway order: " + gatewayOrderId));

                if (payment.getStatus() != PaymentStatus.COMPLETED) {
                    processSuccessfulPayment(payment, gatewayPaymentId);
                }
            }
        } catch (RazorpayException e) {
            throw new RuntimeException("Error verifying webhook signature", e);
        }
    }

    @Transactional
    public void verifyPaymentFallback(VerifyPaymentRequest request) {
        try {
            // Razorpay signature for explicit verification is HMAC-SHA256 of (order_id + "|" + payment_id)
            String payload = request.getRazorpayOrderId() + "|" + request.getRazorpayPaymentId();
            boolean isValid = Utils.verifySignature(payload, request.getRazorpaySignature(), razorpaySecret);
            
            if (!isValid) {
                throw new RuntimeException("Invalid Razorpay payment signature");
            }

            Payment payment = paymentRepository.findByGatewayOrderId(request.getRazorpayOrderId())
                    .orElseThrow(() -> new RuntimeException("Payment not found for gateway order: " + request.getRazorpayOrderId()));

            if (payment.getStatus() != PaymentStatus.COMPLETED) {
                processSuccessfulPayment(payment, request.getRazorpayPaymentId());
            }

        } catch (RazorpayException e) {
            throw new RuntimeException("Error verifying payment signature", e);
        }
    }

    private void processSuccessfulPayment(Payment payment, String gatewayPaymentId) {
        Order order = payment.getOrder();
        Book book = order.getBook();

        if (book.getStatus() != ListingStatus.ACTIVE || book.getQuantity() < 1) {
            // In a real app, we would initiate a refund here since they paid for a sold-out item.
            // For MVP, we throw an error to fail the transaction/log it.
            throw new RuntimeException("Book " + book.getTitle() + " is no longer available. Refund needed.");
        }

        // Deduct inventory and set to SOLD only on successful payment
        book.setQuantity(book.getQuantity() - 1);
        if (book.getQuantity() == 0) {
            book.setStatus(ListingStatus.SOLD);
        }
        bookRepository.save(book);

        // Remove from buyer's cart now that payment is successful
        cartItemRepository.deleteByCartUserIdAndBookId(order.getBuyer().getId(), book.getId());

        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setGatewayPaymentId(gatewayPaymentId);
        paymentRepository.save(payment);

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        // Buyer Notifications
        notificationService.sendNotification(
            order.getBuyer(),
            "ORDER_CONFIRMED",
            "Order Confirmed",
            "Your payment for '" + book.getTitle() + "' was successful.",
            "/orders"
        );
        mailService.sendEmail(
            order.getBuyer().getEmail(),
            "Order Confirmed - " + book.getTitle(),
            "Your payment for '" + book.getTitle() + "' was successful. The seller has been notified."
        );

        // Seller Notifications
        notificationService.sendNotification(
            order.getSeller(),
            "NEW_ORDER",
            "New Order Received",
            "Your book '" + book.getTitle() + "' has been sold!",
            "/orders"
        );
        mailService.sendEmail(
            order.getSeller().getEmail(),
            "You have a new order! - " + book.getTitle(),
            "Your book '" + book.getTitle() + "' has been sold. Please prepare it for handover."
        );
    }
}
