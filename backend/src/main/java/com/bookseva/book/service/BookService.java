package com.bookseva.book.service;

import com.bookseva.book.*;
import com.bookseva.book.dto.BookCreateRequest;
import com.bookseva.book.dto.BookUpdateRequest;
import com.bookseva.common.exception.ImageUploadFailedException;
import com.bookseva.order.repository.OrderRepository;
import com.bookseva.user.User;
import com.bookseva.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookImageRepository bookImageRepository;
    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PlatformTransactionManager transactionManager;

    /**
     * Creates a new book listing.
     *
     * Order of operations (fail-fast):
     *   1. Validate image count (bean validation on other fields already ran in the controller).
     *   2. Validate business rule: price must not exceed MRP.
     *   3. Upload images to Cloudinary — if any upload fails, synchronously delete the
     *      already-uploaded images from that batch before propagating the error.
     *   4. Persist Book + BookImage records in a single @Transactional call so a DB
     *      failure rolls back both without leaving a partial record.
     *
     * No DB write is attempted until ALL images are successfully uploaded.
     */
    public Book createListing(BookCreateRequest request, List<MultipartFile> images, String userEmail) throws IOException {
        // --- Step 1: Validate image count (fail fast, before any I/O) ---
        if (images == null || images.isEmpty()) {
            throw new com.bookseva.common.exception.ImageValidationException("photos", "At least 1 photo is required.");
        }
        if (images.size() > 5) {
            throw new com.bookseva.common.exception.ImageValidationException("photos", "Maximum 5 photos allowed per book listing");
        }

        // --- Step 2: Validate business rules (fail fast, before any I/O) ---
        if (request.getPrice().compareTo(request.getMrp()) > 0) {
            throw new RuntimeException("Selling price cannot be greater than MRP");
        }

        User seller = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        // --- Step 3: Upload images to Cloudinary BEFORE opening DB transaction ---
        // If a failure occurs midway, clean up already-uploaded images and surface a clear error.
        List<String> uploadedUrls = new ArrayList<>();
        try {
            for (MultipartFile file : images) {
                String url = cloudinaryService.uploadImage(file);
                uploadedUrls.add(url);
            }
        } catch (IOException uploadEx) {
            // Clean up any images that were already uploaded in this batch
            for (String url : uploadedUrls) {
                try {
                    cloudinaryService.deleteImage(url);
                } catch (Exception cleanupEx) {
                    log.error("ORPHANED_CLOUDINARY_FILE: Failed to delete Cloudinary image after upload error. URL={}", url, cleanupEx);
                }
            }
            throw new ImageUploadFailedException(
                    "Image upload failed, possibly due to a network issue. Please try again.", uploadEx);
        }

        // --- Step 4: Persist Book + BookImages in a single transaction ---
        // A failure here rolls back both — no partial record is left in the DB.
        return persistListing(request, seller, uploadedUrls);
    }

    /**
     * Persists the Book entity and all associated BookImage records inside a single transaction using TransactionTemplate.
     * This avoids Spring proxy self-invocation issues where @Transactional on internal calls is ignored.
     */
    public Book persistListing(BookCreateRequest request, User seller, List<String> imageUrls) {
        return new TransactionTemplate(transactionManager).execute(status -> {
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
            for (String url : imageUrls) {
                BookImage bookImage = BookImage.builder()
                        .book(book)
                        .url(url)
                        .sortOrder(sortOrder++)
                        .build();
                bookImageRepository.save(bookImage);
            }

            return book;
        });
    }

    public Book updateListing(Long bookId, BookUpdateRequest request, List<MultipartFile> newImages, String userEmail) throws IOException {
        // Fetch book outside transaction to validate first (it will be refetched inside transaction for safety)
        Book preCheckBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (!preCheckBook.getSeller().getEmail().equals(userEmail)) {
            throw new RuntimeException("Not authorized to edit this listing");
        }

        if (orderRepository.existsByBookIdAndStatusNot(bookId, com.bookseva.order.OrderStatus.CANCELLED)) {
            throw new RuntimeException("Cannot edit listing because an order has already been placed for it.");
        }

        if (request.getPrice().compareTo(request.getMrp()) > 0) {
            throw new RuntimeException("Selling price cannot be greater than MRP");
        }

        List<String> keptImageUrls = request.getKeptImageUrls() != null ? request.getKeptImageUrls() : List.of();
        int totalImages = keptImageUrls.size() + (newImages != null ? newImages.size() : 0);
        if (totalImages == 0) {
            throw new com.bookseva.common.exception.ImageValidationException("photos", "At least 1 photo is required.");
        }
        if (totalImages > 5) {
            throw new com.bookseva.common.exception.ImageValidationException("photos", "Maximum 5 photos allowed per book listing");
        }

        // --- Upload new images to Cloudinary BEFORE opening DB transaction ---
        List<String> newlyUploadedUrls = new ArrayList<>();
        if (newImages != null && !newImages.isEmpty()) {
            try {
                for (MultipartFile file : newImages) {
                    String url = cloudinaryService.uploadImage(file);
                    newlyUploadedUrls.add(url);
                }
            } catch (IOException uploadEx) {
                // Clean up any images that were already uploaded in this batch
                for (String url : newlyUploadedUrls) {
                    try {
                        cloudinaryService.deleteImage(url);
                    } catch (Exception cleanupEx) {
                        log.error("ORPHANED_CLOUDINARY_FILE: Failed to delete Cloudinary image after upload error. URL={}", url, cleanupEx);
                    }
                }
                throw new ImageUploadFailedException(
                        "Image upload failed, possibly due to a network issue. Please try again.", uploadEx);
            }
        }

        // --- Execute DB changes inside a single transaction ---
        return new TransactionTemplate(transactionManager).execute(status -> {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("Book not found"));

            book.setTitle(request.getTitle());
            book.setAuthor(request.getAuthor());
            book.setIsbn(request.getIsbn());
            book.setDescription(request.getDescription());
            book.setCategory(request.getCategory());
            book.setCondition(request.getCondition());
            book.setMrp(request.getMrp());
            book.setPrice(request.getPrice());

            book = bookRepository.save(book);

            List<BookImage> existingImages = book.getImages();

            // Remove unkept images from Cloudinary and DB collection
            existingImages.removeIf(image -> {
                if (!keptImageUrls.contains(image.getUrl())) {
                    try {
                        cloudinaryService.deleteImage(image.getUrl());
                    } catch (IOException e) {
                        log.error("Failed to delete image from Cloudinary: {}", image.getUrl(), e);
                    }
                    return true;
                }
                return false;
            });

            // Save new images
            if (!newlyUploadedUrls.isEmpty()) {
                int sortOrder = keptImageUrls.size();
                for (String url : newlyUploadedUrls) {
                    BookImage bookImage = BookImage.builder()
                            .book(book)
                            .url(url)
                            .sortOrder(sortOrder++)
                            .build();
                    bookImageRepository.save(bookImage);
                }
            }

            return book;
        });
    }

    public org.springframework.data.domain.Page<Book> getAllBooks(String title, Category category, ConditionTier condition, java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice, String collegeName, String courseBranch, org.springframework.data.domain.Pageable pageable) {
        return bookRepository.findAll(BookSpecification.filterBooks(title, category, condition, minPrice, maxPrice, collegeName, courseBranch), pageable);
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
        }

        return java.util.Map.of(
            "minPrice", mrp.multiply(minPct).setScale(2, java.math.RoundingMode.HALF_UP),
            "maxPrice", mrp.multiply(maxPct).setScale(2, java.math.RoundingMode.HALF_UP)
        );
    }
}
