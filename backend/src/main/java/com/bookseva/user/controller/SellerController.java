package com.bookseva.user.controller;

import com.bookseva.book.Book;
import com.bookseva.user.dto.SellerProfileResponse;
import com.bookseva.user.dto.SellerReviewResponse;
import com.bookseva.user.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    @GetMapping("/{sellerId}")
    public ResponseEntity<?> getSellerProfile(@PathVariable Long sellerId) {
        SellerProfileResponse profile = sellerService.getSellerProfile(sellerId);
        return ResponseEntity.ok(Map.of("success", true, "data", profile));
    }

    @GetMapping("/{sellerId}/reviews")
    public ResponseEntity<?> getSellerReviews(
            @PathVariable Long sellerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<SellerReviewResponse> reviews = sellerService.getSellerReviews(sellerId, PageRequest.of(page, size));
        return ResponseEntity.ok(Map.of("success", true, "data", reviews));
    }

    @GetMapping("/{sellerId}/books")
    public ResponseEntity<?> getSellerActiveListings(@PathVariable Long sellerId) {
        List<Book> books = sellerService.getSellerActiveListings(sellerId);
        return ResponseEntity.ok(Map.of("success", true, "data", books));
    }
}
