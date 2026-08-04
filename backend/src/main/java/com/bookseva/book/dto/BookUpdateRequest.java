package com.bookseva.book.dto;

import com.bookseva.book.Category;
import com.bookseva.book.ConditionTier;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BookUpdateRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 150)
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    private String isbn;

    @NotBlank(message = "Description is required")
    @Size(min = 20, max = 2000)
    private String description;

    @NotNull(message = "Category is required")
    private Category category;

    @NotNull(message = "Condition is required")
    private ConditionTier condition;

    @NotNull(message = "MRP is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "MRP must be > 0")
    private BigDecimal mrp;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    private List<String> keptImageUrls;
}
