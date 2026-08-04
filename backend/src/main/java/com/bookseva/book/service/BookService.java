package com.bookseva.book.service;

import com.bookseva.book.*;
import com.bookseva.book.dto.BookCreateRequest;
import com.bookseva.book.dto.BookUpdateRequest;
import com.bookseva.order.repository.OrderRepository;
import com.bookseva.user.User;
import com.bookseva.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookImageRepository bookImageRepository;
    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Book createListing(BookCreateRequest request, List<MultipartFile> images, String userEmail) throws IOException {
        if (images == null || images.isEmpty() || images.size() > 5) {
            throw new RuntimeException("1 to 5 images are required.");
        }

        User seller = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        // Price validation against MRP
        if (request.getPrice().compareTo(request.getMrp()) > 0) {
            throw new RuntimeException("Selling price cannot be greater than MRP");
        }
        
        // We warn but don't hard-block condition bounds on the backend per requirements
        
        Book book = Book.builder()
                .seller(seller)
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .category(request.getCategory())
                .condition(request.getCondition())
                .mrp(request.getMrp())
                .price(request.getPrice())
                .description(request.getDescription())
                .quantity(request.getQuantity())
                .status(ListingStatus.ACTIVE)
                .build();

        book = bookRepository.save(book);

        int sortOrder = 0;
        for (MultipartFile file : images) {
            String url = cloudinaryService.uploadImage(file);
            BookImage bookImage = BookImage.builder()
                    .book(book)
                    .url(url)
                    .sortOrder(sortOrder++)
                    .build();
            bookImageRepository.save(bookImage);
        }

        return book;
    }

    @Transactional
    public Book updateListing(Long bookId, BookUpdateRequest request, List<MultipartFile> newImages, String userEmail) throws IOException {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (!book.getSeller().getEmail().equals(userEmail)) {
            throw new RuntimeException("Not authorized to edit this listing");
        }

        if (orderRepository.existsByBookIdAndStatusNot(bookId, com.bookseva.order.OrderStatus.CANCELLED)) {
            throw new RuntimeException("Cannot edit listing because an order has already been placed for it.");
        }

        if (request.getPrice().compareTo(request.getMrp()) > 0) {
            throw new RuntimeException("Selling price cannot be greater than MRP");
        }

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setDescription(request.getDescription());
        book.setCategory(request.getCategory());
        book.setCondition(request.getCondition());
        book.setMrp(request.getMrp());
        book.setPrice(request.getPrice());

        book = bookRepository.save(book);

        // Handle images
        List<BookImage> existingImages = book.getImages();
        List<String> keptImageUrls = request.getKeptImageUrls() != null ? request.getKeptImageUrls() : List.of();

        // Remove unkept images
        for (BookImage image : existingImages) {
            if (!keptImageUrls.contains(image.getUrl())) {
                bookImageRepository.delete(image);
            }
        }

        int totalImages = keptImageUrls.size() + (newImages != null ? newImages.size() : 0);
        if (totalImages == 0 || totalImages > 5) {
            throw new RuntimeException("1 to 5 images are required.");
        }

        // Upload new images
        if (newImages != null && !newImages.isEmpty()) {
            int sortOrder = keptImageUrls.size();
            for (MultipartFile file : newImages) {
                String url = cloudinaryService.uploadImage(file);
                BookImage bookImage = BookImage.builder()
                        .book(book)
                        .url(url)
                        .sortOrder(sortOrder++)
                        .build();
                bookImageRepository.save(bookImage);
            }
        }

        return book;
    }

    public org.springframework.data.domain.Page<Book> getAllBooks(String title, Category category, ConditionTier condition, java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice, String collegeName, String courseBranch, org.springframework.data.domain.Pageable pageable) {
        return bookRepository.findAll((org.springframework.data.jpa.domain.Specification<Book>) (root, query, criteriaBuilder) -> {
            java.util.List<jakarta.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();
            
            if (title != null && !title.isBlank()) {
                String likePattern = "%" + title.toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("isbn")), likePattern)
                ));
            }
            if (category != null) {
                predicates.add(criteriaBuilder.equal(root.get("category"), category));
            }
            if (condition != null) {
                predicates.add(criteriaBuilder.equal(root.get("condition"), condition));
            }
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            
            if (collegeName != null && !collegeName.isBlank() || courseBranch != null && !courseBranch.isBlank()) {
                jakarta.persistence.criteria.Join<Book, User> seller = root.join("seller");
                if (collegeName != null && !collegeName.isBlank()) {
                    predicates.add(criteriaBuilder.equal(seller.get("collegeName"), collegeName));
                }
                if (courseBranch != null && !courseBranch.isBlank()) {
                    predicates.add(criteriaBuilder.equal(seller.get("courseBranch"), courseBranch));
                }
            }
            
            predicates.add(criteriaBuilder.equal(root.get("status"), ListingStatus.ACTIVE));
            
            return criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        }, pageable);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public List<Book> getMyBooks(String email) {
        return bookRepository.findBySellerEmailOrderByCreatedAtDesc(email);
    }

    @Transactional
    public Book updateListingStatus(Long bookId, ListingStatus newStatus, String userEmail) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (!book.getSeller().getEmail().equals(userEmail)) {
            throw new RuntimeException("Not authorized to modify this listing");
        }

        // Sellers shouldn't directly set SOLD status through this, only ACTIVE or REMOVED
        if (newStatus == ListingStatus.SOLD) {
            throw new RuntimeException("Cannot manually mark a book as SOLD");
        }

        book.setStatus(newStatus);
        return bookRepository.save(book);
    }

    public java.util.Map<String, java.math.BigDecimal> getSuggestedPriceRange(ConditionTier condition, java.math.BigDecimal mrp) {
        java.math.BigDecimal minPct = java.math.BigDecimal.ZERO;
        java.math.BigDecimal maxPct = java.math.BigDecimal.ZERO;

        switch (condition) {
            case EXCELLENT -> { minPct = new java.math.BigDecimal("0.60"); maxPct = new java.math.BigDecimal("0.75"); }
            case GOOD -> { minPct = new java.math.BigDecimal("0.40"); maxPct = new java.math.BigDecimal("0.60"); }
            case FAIR -> { minPct = new java.math.BigDecimal("0.20"); maxPct = new java.math.BigDecimal("0.40"); }
            case POOR -> { minPct = new java.math.BigDecimal("0.05"); maxPct = new java.math.BigDecimal("0.15"); }
            case DONATE -> { minPct = java.math.BigDecimal.ZERO; maxPct = java.math.BigDecimal.ZERO; }
        }

        return java.util.Map.of(
            "minPrice", mrp.multiply(minPct).setScale(2, java.math.RoundingMode.HALF_UP),
            "maxPrice", mrp.multiply(maxPct).setScale(2, java.math.RoundingMode.HALF_UP)
        );
    }
}
