package com.bookseva.bootstrap;

import com.bookseva.user.entity.Role;
import com.bookseva.user.entity.User;
import com.bookseva.user.repository.UserRepository;
import com.bookseva.user.entity.UserStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdminSeeder.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${app.admin-seed-email}")
    private String adminEmail;
    @Value("${app.admin-seed-password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = User.builder().name("Super Admin").email(adminEmail).passwordHash(passwordEncoder.encode(adminPassword)).phone("9999999999").collegeName("BookSeva HQ").courseBranch("Administration").role(Role.ADMIN).status(UserStatus.ACTIVE).trustScore(100).build();
            userRepository.save(admin);
            log.info("Admin user seeded successfully with email: {}", adminEmail);
        } else {
            log.info("Admin user with email {} already exists.", adminEmail);
        }
    }

    public AdminSeeder(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
}
