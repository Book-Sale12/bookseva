package com.bookseva.book.entity;

import com.bookseva.user.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;
    @Column(nullable = false, length = 150)
    private String title;
    @Column(nullable = false)
    private String author;
    private String isbn;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;
    @Enumerated(EnumType.STRING)
    @Column(name = "`condition`", nullable = false)
    private ConditionTier condition;
    @Column(nullable = false)
    private BigDecimal mrp;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false, length = 2000)
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ListingStatus status;
    @Column(nullable = false)
    private Integer quantity;
    @Version
    private Long version;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonManagedReference
    private java.util.List<BookImage> images;

    private static Integer $default$quantity() {
        return 1;
    }

    private static java.util.List<BookImage> $default$images() {
        return new java.util.ArrayList<>();
    }


    public static class BookBuilder {
        private Long id;
        private User seller;
        private String title;
        private String author;
        private String isbn;
        private Category category;
        private ConditionTier condition;
        private BigDecimal mrp;
        private BigDecimal price;
        private String description;
        private ListingStatus status;
        private boolean quantity$set;
        private Integer quantity$value;
        private Long version;
        private LocalDateTime createdAt;
        private boolean images$set;
        private java.util.List<BookImage> images$value;

        BookBuilder() {
        }

        /**
         * @return {@code this}.
         */
        public Book.BookBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Book.BookBuilder seller(final User seller) {
            this.seller = seller;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Book.BookBuilder title(final String title) {
            this.title = title;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Book.BookBuilder author(final String author) {
            this.author = author;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Book.BookBuilder isbn(final String isbn) {
            this.isbn = isbn;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Book.BookBuilder category(final Category category) {
            this.category = category;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Book.BookBuilder condition(final ConditionTier condition) {
            this.condition = condition;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Book.BookBuilder mrp(final BigDecimal mrp) {
            this.mrp = mrp;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Book.BookBuilder price(final BigDecimal price) {
            this.price = price;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Book.BookBuilder description(final String description) {
            this.description = description;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Book.BookBuilder status(final ListingStatus status) {
            this.status = status;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Book.BookBuilder quantity(final Integer quantity) {
            this.quantity$value = quantity;
            quantity$set = true;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Book.BookBuilder version(final Long version) {
            this.version = version;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Book.BookBuilder createdAt(final LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Book.BookBuilder images(final java.util.List<BookImage> images) {
            this.images$value = images;
            images$set = true;
            return this;
        }

        public Book build() {
            Integer quantity$value = this.quantity$value;
            if (!this.quantity$set) quantity$value = Book.$default$quantity();
            java.util.List<BookImage> images$value = this.images$value;
            if (!this.images$set) images$value = Book.$default$images();
            return new Book(this.id, this.seller, this.title, this.author, this.isbn, this.category, this.condition, this.mrp, this.price, this.description, this.status, quantity$value, this.version, this.createdAt, images$value);
        }

        @Override
        public String toString() {
            return "Book.BookBuilder(id=" + this.id + ", seller=" + this.seller + ", title=" + this.title + ", author=" + this.author + ", isbn=" + this.isbn + ", category=" + this.category + ", condition=" + this.condition + ", mrp=" + this.mrp + ", price=" + this.price + ", description=" + this.description + ", status=" + this.status + ", quantity$value=" + this.quantity$value + ", version=" + this.version + ", createdAt=" + this.createdAt + ", images$value=" + this.images$value + ")";
        }
    }

    public static Book.BookBuilder builder() {
        return new Book.BookBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public User getSeller() {
        return this.seller;
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

    public String getDescription() {
        return this.description;
    }

    public ListingStatus getStatus() {
        return this.status;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public Long getVersion() {
        return this.version;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public java.util.List<BookImage> getImages() {
        return this.images;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setSeller(final User seller) {
        this.seller = seller;
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

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setStatus(final ListingStatus status) {
        this.status = status;
    }

    public void setQuantity(final Integer quantity) {
        this.quantity = quantity;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setImages(final java.util.List<BookImage> images) {
        this.images = images;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Book)) return false;
        final Book other = (Book) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$quantity = this.getQuantity();
        final Object other$quantity = other.getQuantity();
        if (this$quantity == null ? other$quantity != null : !this$quantity.equals(other$quantity)) return false;
        final Object this$version = this.getVersion();
        final Object other$version = other.getVersion();
        if (this$version == null ? other$version != null : !this$version.equals(other$version)) return false;
        final Object this$seller = this.getSeller();
        final Object other$seller = other.getSeller();
        if (this$seller == null ? other$seller != null : !this$seller.equals(other$seller)) return false;
        final Object this$title = this.getTitle();
        final Object other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
        final Object this$author = this.getAuthor();
        final Object other$author = other.getAuthor();
        if (this$author == null ? other$author != null : !this$author.equals(other$author)) return false;
        final Object this$isbn = this.getIsbn();
        final Object other$isbn = other.getIsbn();
        if (this$isbn == null ? other$isbn != null : !this$isbn.equals(other$isbn)) return false;
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
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) return false;
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
        final Object this$createdAt = this.getCreatedAt();
        final Object other$createdAt = other.getCreatedAt();
        if (this$createdAt == null ? other$createdAt != null : !this$createdAt.equals(other$createdAt)) return false;
        final Object this$images = this.getImages();
        final Object other$images = other.getImages();
        if (this$images == null ? other$images != null : !this$images.equals(other$images)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Book;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $quantity = this.getQuantity();
        result = result * PRIME + ($quantity == null ? 43 : $quantity.hashCode());
        final Object $version = this.getVersion();
        result = result * PRIME + ($version == null ? 43 : $version.hashCode());
        final Object $seller = this.getSeller();
        result = result * PRIME + ($seller == null ? 43 : $seller.hashCode());
        final Object $title = this.getTitle();
        result = result * PRIME + ($title == null ? 43 : $title.hashCode());
        final Object $author = this.getAuthor();
        result = result * PRIME + ($author == null ? 43 : $author.hashCode());
        final Object $isbn = this.getIsbn();
        result = result * PRIME + ($isbn == null ? 43 : $isbn.hashCode());
        final Object $category = this.getCategory();
        result = result * PRIME + ($category == null ? 43 : $category.hashCode());
        final Object $condition = this.getCondition();
        result = result * PRIME + ($condition == null ? 43 : $condition.hashCode());
        final Object $mrp = this.getMrp();
        result = result * PRIME + ($mrp == null ? 43 : $mrp.hashCode());
        final Object $price = this.getPrice();
        result = result * PRIME + ($price == null ? 43 : $price.hashCode());
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        final Object $status = this.getStatus();
        result = result * PRIME + ($status == null ? 43 : $status.hashCode());
        final Object $createdAt = this.getCreatedAt();
        result = result * PRIME + ($createdAt == null ? 43 : $createdAt.hashCode());
        final Object $images = this.getImages();
        result = result * PRIME + ($images == null ? 43 : $images.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Book(id=" + this.getId() + ", seller=" + this.getSeller() + ", title=" + this.getTitle() + ", author=" + this.getAuthor() + ", isbn=" + this.getIsbn() + ", category=" + this.getCategory() + ", condition=" + this.getCondition() + ", mrp=" + this.getMrp() + ", price=" + this.getPrice() + ", description=" + this.getDescription() + ", status=" + this.getStatus() + ", quantity=" + this.getQuantity() + ", version=" + this.getVersion() + ", createdAt=" + this.getCreatedAt() + ", images=" + this.getImages() + ")";
    }

    public Book() {
        this.quantity = Book.$default$quantity();
        this.images = Book.$default$images();
    }

    public Book(final Long id, final User seller, final String title, final String author, final String isbn, final Category category, final ConditionTier condition, final BigDecimal mrp, final BigDecimal price, final String description, final ListingStatus status, final Integer quantity, final Long version, final LocalDateTime createdAt, final java.util.List<BookImage> images) {
        this.id = id;
        this.seller = seller;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
        this.condition = condition;
        this.mrp = mrp;
        this.price = price;
        this.description = description;
        this.status = status;
        this.quantity = quantity;
        this.version = version;
        this.createdAt = createdAt;
        this.images = images;
    }
}
