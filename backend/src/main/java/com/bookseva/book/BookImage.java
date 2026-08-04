//package com.bookseva.book;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Table(name = "book_images")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class BookImage {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "book_id", nullable = false)
//    @com.fasterxml.jackson.annotation.JsonBackReference
//    private Book book;
//
//    @Column(nullable = false)
//    private String url;
//
//    @Column(name = "sort_order", nullable = false)
//    @Builder.Default
//    private Integer sortOrder = 0;
//}


//updated code by chatgpt

package com.bookseva.book;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Column(name = "sort_order", nullable =false)
    @Builder.Default
    private Integer sortOrder = 0;
}