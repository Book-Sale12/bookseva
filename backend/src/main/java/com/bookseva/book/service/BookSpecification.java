package com.bookseva.book.service;

import com.bookseva.book.Book;
import com.bookseva.book.BookImage;
import com.bookseva.book.Category;
import com.bookseva.book.ConditionTier;
import com.bookseva.book.ListingStatus;
import com.bookseva.user.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BookSpecification {

    public static Specification<Book> filterBooks(String title, Category category, ConditionTier condition,
                                                  BigDecimal minPrice, BigDecimal maxPrice,
                                                  String collegeName, String courseBranch) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isBlank()) {
                String likePattern = "%" + title.toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("isbn")), likePattern)
                ));
            }
            if (category != null) {
                predicates.add(criteriaBuilder.equal(root.get("category"), category));
            }
            if (condition != null) {
                predicates.add(criteriaBuilder.equal(root.get("condition"), condition));
            }
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            if ((collegeName != null && !collegeName.isBlank()) || (courseBranch != null && !courseBranch.isBlank())) {
                Join<Book, User> seller = root.join("seller");
                if (collegeName != null && !collegeName.isBlank()) {
                    predicates.add(criteriaBuilder.equal(seller.get("collegeName"), collegeName));
                }
                if (courseBranch != null && !courseBranch.isBlank()) {
                    predicates.add(criteriaBuilder.equal(seller.get("courseBranch"), courseBranch));
                }
            }

            predicates.add(criteriaBuilder.equal(root.get("status"), ListingStatus.ACTIVE));

            // Safety-net: never show listings with zero images
            Subquery<Long> imageCountSubquery = query.subquery(Long.class);
            Root<BookImage> imageRoot = imageCountSubquery.from(BookImage.class);
            imageCountSubquery.select(criteriaBuilder.count(imageRoot))
                    .where(criteriaBuilder.equal(imageRoot.get("book"), root));
            predicates.add(criteriaBuilder.greaterThan(imageCountSubquery, 0L));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
