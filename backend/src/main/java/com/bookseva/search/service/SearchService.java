package com.bookseva.search.service;

import com.bookseva.book.entity.Book;
import com.bookseva.book.repository.BookRepository;
import com.bookseva.book.entity.Category;
import com.bookseva.book.entity.ConditionTier;
import com.bookseva.search.specification.BookSearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class SearchService {
    private final BookRepository bookRepository;

    public Page<Book> searchBooks(String query, Category category, ConditionTier condition, BigDecimal minPrice, BigDecimal maxPrice, String collegeName, String courseBranch, Boolean availableOnly, Pageable pageable) {
        return bookRepository.findAll(BookSearchSpecification.search(query, category, condition, minPrice, maxPrice, collegeName, courseBranch, availableOnly), pageable);
    }

    public SearchService(final BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
}
