package com.bookseva.payment.controller;

import com.bookseva.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("x-razorpay-signature") String signature
    ) {
        boolean success = paymentService.handleWebhook(payload, signature);
        if (!success) {
            return ResponseEntity.badRequest().body(java.util.Map.of("success", false, "message", "Webhook verification failed"));
        }
        return ResponseEntity.ok(java.util.Map.of("success", true));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody com.bookseva.payment.dto.VerifyPaymentRequest request) {
        paymentService.verifyPaymentFallback(request);
        return ResponseEntity.ok().build();
    }
}
