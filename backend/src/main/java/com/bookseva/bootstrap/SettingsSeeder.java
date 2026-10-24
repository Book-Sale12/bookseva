package com.bookseva.bootstrap;
import com.bookseva.user.entity.User;


import com.bookseva.admin.entity.PlatformSetting;
import com.bookseva.admin.repository.PlatformSettingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class SettingsSeeder implements CommandLineRunner {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SettingsSeeder.class);
    private final PlatformSettingRepository platformSettingRepository;

    @Override
    public void run(String... args) {
        if (platformSettingRepository.count() == 0) {
            log.info("Seeding default platform settings...");
            platformSettingRepository.save(PlatformSetting.builder().key("PLATFORM_FEE_PCT").value("5.0").description("Percentage fee taken by the platform per transaction").build());
            platformSettingRepository.save(PlatformSetting.builder().key("LISTING_EXPIRY_DAYS").value("30").description("Number of days before a listing automatically expires").build());
            platformSettingRepository.save(PlatformSetting.builder().key("MAX_REPORTS_BEFORE_SUSPENSION").value("5").description("Threshold for automatic user suspension based on reports").build());
            platformSettingRepository.save(PlatformSetting.builder().key("MIN_TRUST_SCORE").value("50").description("Minimum trust score required to create new listings").build());
            log.info("Platform settings seeded successfully.");
        } else {
            log.info("Platform settings already exist, skipping seeding.");
        }
    }

    public SettingsSeeder(final PlatformSettingRepository platformSettingRepository) {
        this.platformSettingRepository = platformSettingRepository;
    }
}
