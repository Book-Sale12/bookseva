package com.bookseva.config;

import com.bookseva.admin.PlatformSetting;
import com.bookseva.admin.repository.PlatformSettingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(3)
public class SettingsSeeder implements CommandLineRunner {

    private final PlatformSettingRepository platformSettingRepository;

    @Override
    public void run(String... args) {
        if (platformSettingRepository.count() == 0) {
            log.info("Seeding default platform settings...");

            platformSettingRepository.save(PlatformSetting.builder()
                    .key("PLATFORM_FEE_PCT")
                    .value("5.0")
                    .description("Percentage fee taken by the platform per transaction")
                    .build());

            platformSettingRepository.save(PlatformSetting.builder()
                    .key("LISTING_EXPIRY_DAYS")
                    .value("30")
                    .description("Number of days before a listing automatically expires")
                    .build());

            platformSettingRepository.save(PlatformSetting.builder()
                    .key("MAX_REPORTS_BEFORE_SUSPENSION")
                    .value("5")
                    .description("Threshold for automatic user suspension based on reports")
                    .build());

            platformSettingRepository.save(PlatformSetting.builder()
                    .key("MIN_TRUST_SCORE")
                    .value("50")
                    .description("Minimum trust score required to create new listings")
                    .build());

            log.info("Platform settings seeded successfully.");
        } else {
            log.info("Platform settings already exist, skipping seeding.");
        }
    }
}
