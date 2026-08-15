// package com.bookseva.search.repository;

// import com.bookseva.book.Book;
// import com.bookseva.book.Category;
// import com.bookseva.book.ConditionTier;
// import com.bookseva.book.ListingStatus;
// import org.springframework.data.jpa.domain.Specification;
// import org.springframework.util.StringUtils;

// import java.math.BigDecimal;
// import jakarta.persistence.criteria.JoinType;

// public class BookSpecification {

//     public static Specification<Book> search(String query, Category category, ConditionTier condition, BigDecimal minPrice, BigDecimal maxPrice, String collegeName, Boolean availableOnly) {
//         return (root, cq, cb) -> {
//             Specification<Book> spec = Specification.where(null);

//             if (StringUtils.hasText(query)) {
//                 String likeQuery = "%" + query.toLowerCase() + "%";
//                 spec = spec.and((r, q, c) -> c.or(
//                         c.like(c.lower(r.get("title")), likeQuery),
//                         c.like(c.lower(r.get("author")), likeQuery),
//                         c.like(c.lower(r.get("isbn")), likeQuery),
//                         c.like(c.lower(r.get("description")), likeQuery)
//                 ));
//             }

//             if (category != null) {
//                 spec = spec.and((r, q, c) -> c.equal(r.get("category"), category));
//             }

//             if (condition != null) {
//                 spec = spec.and((r, q, c) -> c.equal(r.get("condition"), condition));
//             }

//             if (minPrice != null) {
//                 spec = spec.and((r, q, c) -> c.greaterThanOrEqualTo(r.get("price"), minPrice));
//             }

//             if (maxPrice != null) {
//                 spec = spec.and((r, q, c) -> c.lessThanOrEqualTo(r.get("price"), maxPrice));
//             }

//             if (StringUtils.hasText(collegeName)) {
//                 spec = spec.and((r, q, c) -> {
//                     var sellerJoin = r.join("seller", JoinType.INNER);
//                     return c.equal(sellerJoin.get("collegeName"), collegeName);
//                 });
//             }

//             if (Boolean.TRUE.equals(availableOnly)) {
//                 spec = spec.and((r, q, c) -> c.equal(r.get("status"), ListingStatus.ACTIVE));
//             }

//             return spec.toPredicate(root, cq, cb);
//         };
//     }
// }

package com.bookseva.search.repository;

import com.bookseva.book.Book;
import com.bookseva.book.Category;
import com.bookseva.book.ConditionTier;
import com.bookseva.book.ListingStatus;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

public class BookSpecification {

    public static Specification<Book> search(
            String query,
            Category category,
            ConditionTier condition,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String collegeName,
            String courseBranch,
            Boolean availableOnly) {

        Specification<Book> spec = (root, cq, cb) -> cb.conjunction();

        if (StringUtils.hasText(query)) {
            String likeQuery = "%" + query.toLowerCase() + "%";
            spec = spec.and((r, q, c) -> c.or(
                    c.like(c.lower(r.get("title")), likeQuery),
                    c.like(c.lower(r.get("author")), likeQuery),
                    c.like(c.lower(r.get("isbn")), likeQuery),
                    c.like(c.lower(r.get("description")), likeQuery)));
        }

        if (category != null) {
            spec = spec.and((r, q, c) -> c.equal(r.get("category"), category));
        }

        if (condition != null) {
            spec = spec.and((r, q, c) -> c.equal(r.get("condition"), condition));
        }

        if (minPrice != null) {
            spec = spec.and((r, q, c) -> c.greaterThanOrEqualTo(r.get("price"), minPrice));
        }

        if (maxPrice != null) {
            spec = spec.and((r, q, c) -> c.lessThanOrEqualTo(r.get("price"), maxPrice));
        }

        if (StringUtils.hasText(collegeName) || StringUtils.hasText(courseBranch)) {
            spec = spec.and((r, q, c) -> {
                var sellerJoin = r.join("seller", JoinType.INNER);
                var pred = c.conjunction();
                if (StringUtils.hasText(collegeName)) {
                    pred = c.and(pred, c.equal(sellerJoin.get("collegeName"), collegeName));
                }
                if (StringUtils.hasText(courseBranch)) {
                    pred = c.and(pred, c.equal(sellerJoin.get("courseBranch"), courseBranch));
                }
                return pred;
            });
        }

        if (Boolean.TRUE.equals(availableOnly)) {
            spec = spec.and((r, q, c) -> c.equal(r.get("status"), ListingStatus.ACTIVE));
        }

        return spec;
    }
}