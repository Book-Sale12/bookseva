package com.bookseva.book.dto;

import com.bookseva.book.Category;
import com.bookseva.book.ConditionTier;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BookCreateRequest {

    @NotBlank
    @Size(min = 3, max = 150)
    private String title;

    @NotBlank
    private String author;

    private String isbn;

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
    private Integer quantity = 1;
}
