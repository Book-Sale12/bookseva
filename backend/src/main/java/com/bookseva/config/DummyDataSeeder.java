package com.bookseva.config;

import com.bookseva.book.Book;
import com.bookseva.book.BookImage;
import com.bookseva.book.BookImageRepository;
import com.bookseva.book.BookRepository;
import com.bookseva.book.Category;
import com.bookseva.book.ConditionTier;
import com.bookseva.book.ListingStatus;
import com.bookseva.user.Role;
import com.bookseva.user.User;
import com.bookseva.user.UserRepository;
import com.bookseva.user.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(2)
public class DummyDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookImageRepository bookImageRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        User alice;
        if (userRepository.count() < 3) {
            log.info("Seeding dummy users...");
            alice = createUser("Alice Student", "alice@college.edu", "Computer Science");
            createUser("Bob Engineer", "bob@college.edu", "Mechanical Engineering");
            createUser("Charlie Reader", "charlie@college.edu", "Arts & Humanities");
        } else {
            alice = userRepository.findByEmail("alice@college.edu").orElseGet(() -> 
                createUser("Alice Student", "alice@college.edu", "Computer Science")
            );
        }

        if (bookRepository.count() < 50) {
            log.info("Seeding 50 dummy books...");
            String[] titles = {"Introduction to Algorithms", "Operating System Concepts", "Engineering Mechanics", "Thermodynamics", "A History of the Modern World", "The Art of Public Speaking", "Calculus Early Transcendentals", "Database System Concepts", "Artificial Intelligence", "Data Structures and Algorithms"};
            String[] authors = {"Thomas H. Cormen", "Abraham Silberschatz", "R.C. Hibbeler", "Yunus A. Cengel", "Palmer", "Stephen Lucas", "James Stewart", "Silberschatz", "Stuart Russell", "Narasimha Karumanchi"};
            Category[] categories = {Category.ENGINEERING, Category.MEDICAL, Category.SCIENCE, Category.COMMERCE, Category.ARTS_HUMANITIES, Category.DIPLOMA, Category.LAW, Category.MANAGEMENT, Category.COMPUTER_APPLICATIONS, Category.OTHER};
            ConditionTier[] conditions = {ConditionTier.EXCELLENT, ConditionTier.GOOD, ConditionTier.FAIR, ConditionTier.POOR, ConditionTier.DONATE};

            for (int i = 0; i < 50; i++) {
                String title = titles[i % titles.length] + " " + (i + 1);
                String author = authors[i % authors.length];
                Category category = categories[i % categories.length];
                ConditionTier condition = conditions[i % conditions.length];
                BigDecimal mrp = BigDecimal.valueOf(1000 + (i * 50));
                BigDecimal price = BigDecimal.valueOf(500 + (i * 20));
                
                createBook(alice, title, author, category, condition, mrp, price, "This is a dummy description for book " + (i + 1));
            }
            log.info("50 Dummy books seeded successfully.");
        } else {
            log.info("Dummy data already exists, skipping seeding.");
        }
    }

    private User createUser(String name, String email, String branch) {
        User user = User.builder()
                .name(name)
                .email(email)
                .passwordHash(passwordEncoder.encode("password123"))
                .phone("9876543210")
                .collegeName("Demo University")
                .courseBranch(branch)
                .role(Role.STUDENT)
                .status(UserStatus.ACTIVE)
                .trustScore(100)
                .build();
        return userRepository.save(user);
    }

    private void createBook(User seller, String title, String author, Category category, ConditionTier condition, BigDecimal mrp, BigDecimal price, String desc) {
        Book book = Book.builder()
                .title(title)
                .author(author)
                .category(category)
                .condition(condition)
                .mrp(mrp)
                .price(price)
                .description(desc)
                .seller(seller)
                .status(ListingStatus.ACTIVE)
                .quantity(1)
                .build();

        Book savedBook = bookRepository.save(book);

        BookImage image = BookImage.builder()
                .book(savedBook)
                .url("https://placehold.co/600x800/png?text=" + title.replace(" ", "+"))
                .sortOrder(0)
                .build();

        bookImageRepository.save(image);
    }
}
