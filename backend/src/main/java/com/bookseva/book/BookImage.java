<<<<<<< HEAD
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

=======
>>>>>>> 7fb50eb898dfa7106f0876c78aeafb3c656106d2
package com.bookseva.book;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
<<<<<<< HEAD
@Table(name = "book_images")
=======
@Table(name = "book_images", indexes = {
        @Index(name = "idx_book_images_book_id", columnList = "book_id")
})
>>>>>>> 7fb50eb898dfa7106f0876c78aeafb3c656106d2
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

<<<<<<< HEAD
    @Column(name = "sort_order", nullable =false)
=======
    @Column(name = "sort_order", nullable = false)
>>>>>>> 7fb50eb898dfa7106f0876c78aeafb3c656106d2
    @Builder.Default
    private Integer sortOrder = 0;
}