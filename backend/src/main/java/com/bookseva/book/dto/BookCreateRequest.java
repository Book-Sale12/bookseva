package com.bookseva.book.dto;

import com.bookseva.book.Category;
import com.bookseva.book.ConditionTier;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
<<<<<<< HEAD
import java.util.List;
=======
>>>>>>> 7fb50eb898dfa7106f0876c78aeafb3c656106d2

@Data
public class BookCreateRequest {

<<<<<<< HEAD
    @NotBlank
    @Size(min = 3, max = 150)
    private String title;

    @NotBlank
=======
    @NotBlank(message = "Book name must not be empty")
    @Size(min = 2, max = 150, message = "Book name must be between 2 and 150 characters")
    @Pattern(
        regexp = "^(?!\\d+$).+$",
        message = "Book name cannot be purely numeric"
    )
    private String title;

    @NotBlank(message = "Author must not be empty")
    @Size(min = 2, message = "Author name must be at least 2 characters")
    @Pattern(
        regexp = "^(?!\\d+$).+$",
        message = "Author cannot be purely numeric"
    )
>>>>>>> 7fb50eb898dfa7106f0876c78aeafb3c656106d2
    private String author;

    private String isbn;

<<<<<<< HEAD
    @NotNull
    private Category category;

    @NotNull
    private ConditionTier condition;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal mrp;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    @NotBlank
    @Size(min = 20, max = 2000)
    private String description;

    @Min(1)
=======
    @NotNull(message = "Category is required")
    private Category category;

    @NotNull(message = "Condition is required")
    private ConditionTier condition;

    @NotNull(message = "MRP is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "MRP must be greater than 0")
    private BigDecimal mrp;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotBlank(message = "Description must not be empty")
    @Size(min = 10, max = 150, message = "Description must be between 10 and 150 characters")
    @Pattern(
        // Combined rules:
        //  1. ^(?!\d+$)           — not purely numeric
        //  2. (?![^a-zA-Z0-9]+$)  — not purely special/non-alphanumeric characters
        //  3. (?!.*(\d)\1{2,})    — no run of 3+ identical consecutive digits
        //  4. (?!.*([^a-zA-Z0-9\s])\2{2,}) — no run of 3+ identical consecutive special chars
        regexp = "^(?!\\d+$)(?![^a-zA-Z0-9]+$)(?!.*(\\d)\\1{2,})(?!.*([^a-zA-Z0-9\\s])\\2{2,})[\\s\\S]+$",
        message = "Description cannot contain repeated numbers or special characters"
    )
    private String description;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
>>>>>>> 7fb50eb898dfa7106f0876c78aeafb3c656106d2
    private Integer quantity = 1;
}
