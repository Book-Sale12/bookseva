package com.bookseva.cart.controller;

import com.bookseva.cart.dto.AddToCartRequest;
import com.bookseva.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<?> getCart(Authentication authentication) {
        return ResponseEntity.ok(Map.of("success", true, "data", cartService.getCart(authentication.getName())));
    }

    @PostMapping("/items")
    public ResponseEntity<?> addToCart(@Valid @RequestBody AddToCartRequest request, Authentication authentication) {
        return ResponseEntity.ok(Map.of("success", true, "data", cartService.addToCart(request.getBookId(), authentication.getName())));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long id, Authentication authentication) {
        cartService.removeFromCart(id, authentication.getName());
        return ResponseEntity.ok(Map.of("success", true, "message", "Item removed from cart"));
    }
}
