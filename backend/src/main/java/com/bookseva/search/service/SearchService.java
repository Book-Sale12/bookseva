package com.bookseva.search.service;

import com.bookseva.book.Book;
import com.bookseva.book.BookRepository;
import com.bookseva.book.Category;
import com.bookseva.book.ConditionTier;
import com.bookseva.search.repository.BookSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final BookRepository bookRepository;

    public Page<Book> searchBooks(String query, Category category, ConditionTier condition, BigDecimal minPrice, BigDecimal maxPrice, String collegeName, String courseBranch, Boolean availableOnly, Pageable pageable) {
        return bookRepository.findAll(BookSpecification.search(query, category, condition, minPrice, maxPrice, collegeName, courseBranch, availableOnly), pageable);
    }
}
