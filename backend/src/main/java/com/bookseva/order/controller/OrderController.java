package com.bookseva.order.controller;

import com.bookseva.order.dto.CheckoutRequest;
import com.bookseva.order.service.OrderService;
import com.razorpay.RazorpayException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@Valid @RequestBody CheckoutRequest request, Authentication authentication) throws RazorpayException {
        var payments = orderService.checkout(request.getCartItemIds(), authentication.getName());
        return ResponseEntity.ok(Map.of("success", true, "data", payments));
    }

    @GetMapping("/mine")
    public ResponseEntity<?> getMyOrders(Authentication authentication) {
        var orders = orderService.getOrdersForBuyer(authentication.getName());
        return ResponseEntity.ok(Map.of("success", true, "data", orders));
    }

    @GetMapping("/sales")
    public ResponseEntity<?> getMySales(Authentication authentication) {
        var orders = orderService.getOrdersForSeller(authentication.getName());
        return ResponseEntity.ok(Map.of("success", true, "data", orders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@org.springframework.web.bind.annotation.PathVariable Long id, Authentication authentication) {
        var order = orderService.getOrderById(id, authentication.getName());
        return ResponseEntity.ok(Map.of("success", true, "data", order));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@org.springframework.web.bind.annotation.PathVariable Long id, Authentication authentication) {
        orderService.cancelOrder(id, authentication.getName());
        return ResponseEntity.ok(Map.of("success", true, "data", "Order cancelled"));
    }

    @org.springframework.web.bind.annotation.PatchMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(
            @org.springframework.web.bind.annotation.PathVariable Long id,
            @Valid @RequestBody com.bookseva.order.dto.OrderStatusUpdateRequest request,
            Authentication authentication
    ) {
        orderService.updateOrderStatus(id, request.getNewStatus(), authentication.getName());
        return ResponseEntity.ok(Map.of("success", true, "data", "Status updated successfully"));
    }
}
