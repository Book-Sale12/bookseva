package com.bookseva.book.dto;
import com.bookseva.book.entity.Book;
import com.bookseva.message.entity.Message;


import com.bookseva.book.entity.Category;
import com.bookseva.book.entity.ConditionTier;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public class BookUpdateRequest {
    @NotBlank(message = "Book name must not be empty")
    @Size(min = 2, max = 150, message = "Book name must be between 2 and 150 characters")
    @Pattern(regexp = "^(?!\\d+$).+$", message = "Book name cannot be purely numeric")
    private String title;
    @NotBlank(message = "Author must not be empty")
    @Size(min = 2, message = "Author name must be at least 2 characters")
    @Pattern(regexp = "^(?!\\d+$).+$", message = "Author cannot be purely numeric")
    private String author;
    private String isbn;
    // Combined rules:
    //  1. ^(?!\d+$)           — not purely numeric
    //  2. (?![^a-zA-Z0-9]+$)  — not purely special/non-alphanumeric characters
    //  3. (?!.*(\d)\1{2,})    — no run of 3+ identical consecutive digits
    //  4. (?!.*([^a-zA-Z0-9\s])\2{2,}) — no run of 3+ identical consecutive special chars
    @NotBlank(message = "Description must not be empty")
    @Size(min = 10, max = 150, message = "Description must be between 10 and 150 characters")
    @Pattern(regexp = "^(?!\\d+$)(?![^a-zA-Z0-9]+$)(?!.*(\\d)\\1{2,})(?!.*([^a-zA-Z0-9\\s])\\2{2,})[\\s\\S]+$", message = "Description cannot contain repeated numbers or special characters")
    private String description;
    @NotNull(message = "Category is required")
    private Category category;
    @NotNull(message = "Condition is required")
    private ConditionTier condition;
    @NotNull(message = "MRP is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "MRP must be greater than 0")
    private BigDecimal mrp;
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    private List<String> keptImageUrls;

    public BookUpdateRequest() {
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public String getDescription() {
        return this.description;
    }

    public Category getCategory() {
        return this.category;
    }

    public ConditionTier getCondition() {
        return this.condition;
    }

    public BigDecimal getMrp() {
        return this.mrp;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public List<String> getKeptImageUrls() {
        return this.keptImageUrls;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public void setIsbn(final String isbn) {
        this.isbn = isbn;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setCategory(final Category category) {
        this.category = category;
    }

    public void setCondition(final ConditionTier condition) {
        this.condition = condition;
    }

    public void setMrp(final BigDecimal mrp) {
        this.mrp = mrp;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public void setKeptImageUrls(final List<String> keptImageUrls) {
        this.keptImageUrls = keptImageUrls;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof BookUpdateRequest)) return false;
        final BookUpdateRequest other = (BookUpdateRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$title = this.getTitle();
        final Object other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
        final Object this$author = this.getAuthor();
        final Object other$author = other.getAuthor();
        if (this$author == null ? other$author != null : !this$author.equals(other$author)) return false;
        final Object this$isbn = this.getIsbn();
        final Object other$isbn = other.getIsbn();
        if (this$isbn == null ? other$isbn != null : !this$isbn.equals(other$isbn)) return false;
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) return false;
        final Object this$category = this.getCategory();
        final Object other$category = other.getCategory();
        if (this$category == null ? other$category != null : !this$category.equals(other$category)) return false;
        final Object this$condition = this.getCondition();
        final Object other$condition = other.getCondition();
        if (this$condition == null ? other$condition != null : !this$condition.equals(other$condition)) return false;
        final Object this$mrp = this.getMrp();
        final Object other$mrp = other.getMrp();
        if (this$mrp == null ? other$mrp != null : !this$mrp.equals(other$mrp)) return false;
        final Object this$price = this.getPrice();
        final Object other$price = other.getPrice();
        if (this$price == null ? other$price != null : !this$price.equals(other$price)) return false;
        final Object this$keptImageUrls = this.getKeptImageUrls();
        final Object other$keptImageUrls = other.getKeptImageUrls();
        if (this$keptImageUrls == null ? other$keptImageUrls != null : !this$keptImageUrls.equals(other$keptImageUrls)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof BookUpdateRequest;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $title = this.getTitle();
        result = result * PRIME + ($title == null ? 43 : $title.hashCode());
        final Object $author = this.getAuthor();
        result = result * PRIME + ($author == null ? 43 : $author.hashCode());
        final Object $isbn = this.getIsbn();
        result = result * PRIME + ($isbn == null ? 43 : $isbn.hashCode());
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        final Object $category = this.getCategory();
        result = result * PRIME + ($category == null ? 43 : $category.hashCode());
        final Object $condition = this.getCondition();
        result = result * PRIME + ($condition == null ? 43 : $condition.hashCode());
        final Object $mrp = this.getMrp();
        result = result * PRIME + ($mrp == null ? 43 : $mrp.hashCode());
        final Object $price = this.getPrice();
        result = result * PRIME + ($price == null ? 43 : $price.hashCode());
        final Object $keptImageUrls = this.getKeptImageUrls();
        result = result * PRIME + ($keptImageUrls == null ? 43 : $keptImageUrls.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "BookUpdateRequest(title=" + this.getTitle() + ", author=" + this.getAuthor() + ", isbn=" + this.getIsbn() + ", description=" + this.getDescription() + ", category=" + this.getCategory() + ", condition=" + this.getCondition() + ", mrp=" + this.getMrp() + ", price=" + this.getPrice() + ", keptImageUrls=" + this.getKeptImageUrls() + ")";
    }
}
