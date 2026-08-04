package com.bookseva.config;

import com.bookseva.user.Role;
import com.bookseva.user.User;
import com.bookseva.user.UserRepository;
import com.bookseva.user.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin-seed-email}")
    private String adminEmail;

    @Value("${app.admin-seed-password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = User.builder()
                    .name("Super Admin")
                    .email(adminEmail)
                    .passwordHash(passwordEncoder.encode(adminPassword))
                    .phone("9999999999")
                    .collegeName("BookSeva HQ")
                    .courseBranch("Administration")
                    .role(Role.ADMIN)
                    .status(UserStatus.ACTIVE)
                    .trustScore(100)
                    .build();
            userRepository.save(admin);
            log.info("Admin user seeded successfully with email: {}", adminEmail);
        } else {
            log.info("Admin user with email {} already exists.", adminEmail);
        }
    }
}
