package com.bookseva.book.repository;
import com.bookseva.book.entity.Book;
import com.bookseva.book.entity.BookImage;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookImageRepository extends JpaRepository<BookImage, Long> {
    List<BookImage> findByBookIdOrderBySortOrderAsc(Long bookId);
}
