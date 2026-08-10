package com.bookseva.book.controller;

import com.bookseva.book.Book;
import com.bookseva.book.dto.BookCreateRequest;
import com.bookseva.book.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> createListing(
            @Valid @RequestPart("data") BookCreateRequest request,
            @RequestPart("images") List<MultipartFile> images,
            Authentication authentication
    ) throws IOException {
        String userEmail = authentication.getName();
        Book book = bookService.createListing(request, images, userEmail);
        return ResponseEntity.ok(Map.of("success", true, "data", book));
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateListing(
            @PathVariable Long id,
            @Valid @RequestPart("data") com.bookseva.book.dto.BookUpdateRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            Authentication authentication
    ) throws IOException {
        String userEmail = authentication.getName();
        Book book = bookService.updateListing(id, request, images, userEmail);
        return ResponseEntity.ok(Map.of("success", true, "data", book));
    }

    @GetMapping
    public ResponseEntity<?> getAllBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) com.bookseva.book.Category category,
            @RequestParam(required = false) com.bookseva.book.ConditionTier condition,
            @RequestParam(required = false) java.math.BigDecimal minPrice,
            @RequestParam(required = false) java.math.BigDecimal maxPrice,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String collegeName,
            @RequestParam(required = false) String courseBranch,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        size = Math.min(size, 100);
        org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.by("createdAt").descending();
        if (sortBy != null) {
            switch (sortBy) {
                case "PRICE_ASC" -> sort = org.springframework.data.domain.Sort.by("price").ascending();
                case "PRICE_DESC" -> sort = org.springframework.data.domain.Sort.by("price").descending();
                case "CONDITION" -> sort = org.springframework.data.domain.Sort.by("condition").ascending();
                case "NEWEST" -> sort = org.springframework.data.domain.Sort.by("createdAt").descending();
            }
        }
        
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, sort);
        org.springframework.data.domain.Page<Book> books = bookService.getAllBooks(title, category, condition, minPrice, maxPrice, collegeName, courseBranch, pageable);
        return ResponseEntity.ok(Map.of("success", true, "data", books));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        var book = bookService.getBookById(id);
        return ResponseEntity.ok(Map.of("success", true, "data", book));
    }

    @GetMapping("/suggested-price")
    public ResponseEntity<?> getSuggestedPrice(
            @RequestParam com.bookseva.book.ConditionTier condition,
            @RequestParam java.math.BigDecimal mrp) {
        var range = bookService.getSuggestedPriceRange(condition, mrp);
        return ResponseEntity.ok(Map.of("success", true, "data", range));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyBooks(Authentication authentication) {
        String email = authentication.getName();
        List<Book> books = bookService.getMyBooks(email);
        return ResponseEntity.ok(Map.of("success", true, "data", books));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateListingStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            Authentication authentication
    ) {
        String email = authentication.getName();
        String statusStr = body.get("status");
        if (statusStr == null || statusStr.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", Map.of("message", "Status field is required")));
        }
        com.bookseva.book.ListingStatus status;
        try {
            status = com.bookseva.book.ListingStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", Map.of("message", "Invalid status value: " + statusStr)));
        }
        Book book = bookService.updateListingStatus(id, status, email);
        return ResponseEntity.ok(Map.of("success", true, "data", book));
    }
}
