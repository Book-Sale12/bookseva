package com.bookseva.book.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "book_images", indexes = {@Index(name = "idx_book_images_book_id", columnList = "book_id")})
public class BookImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    @JsonBackReference
    private Book book;
    @Column(nullable = false)
    private String url;
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    private static Integer $default$sortOrder() {
        return 0;
    }


    public static class BookImageBuilder {
        private Long id;
        private Book book;
        private String url;
        private boolean sortOrder$set;
        private Integer sortOrder$value;

        BookImageBuilder() {
        }

        /**
         * @return {@code this}.
         */
        public BookImage.BookImageBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public BookImage.BookImageBuilder book(final Book book) {
            this.book = book;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public BookImage.BookImageBuilder url(final String url) {
            this.url = url;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public BookImage.BookImageBuilder sortOrder(final Integer sortOrder) {
            this.sortOrder$value = sortOrder;
            sortOrder$set = true;
            return this;
        }

        public BookImage build() {
            Integer sortOrder$value = this.sortOrder$value;
            if (!this.sortOrder$set) sortOrder$value = BookImage.$default$sortOrder();
            return new BookImage(this.id, this.book, this.url, sortOrder$value);
        }

        @Override
        public String toString() {
            return "BookImage.BookImageBuilder(id=" + this.id + ", book=" + this.book + ", url=" + this.url + ", sortOrder$value=" + this.sortOrder$value + ")";
        }
    }

    public static BookImage.BookImageBuilder builder() {
        return new BookImage.BookImageBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public Book getBook() {
        return this.book;
    }

    public String getUrl() {
        return this.url;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setBook(final Book book) {
        this.book = book;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public void setSortOrder(final Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public BookImage() {
        this.sortOrder = BookImage.$default$sortOrder();
    }

    public BookImage(final Long id, final Book book, final String url, final Integer sortOrder) {
        this.id = id;
        this.book = book;
        this.url = url;
        this.sortOrder = sortOrder;
    }
}
