package com.bookseva.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    java.util.List<Book> findBySellerEmailOrderByCreatedAtDesc(String email);
    
    long countBySellerIdAndStatus(Long sellerId, ListingStatus status);
    long countBySellerId(Long sellerId);
    long countByStatus(ListingStatus status);
    java.util.List<Book> findBySellerIdAndStatusOrderByCreatedAtDesc(Long sellerId, ListingStatus status);

    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("SELECT b FROM Book b WHERE b.id = :id")
    java.util.Optional<Book> findByIdWithLock(@org.springframework.data.repository.query.Param("id") Long id);
}
