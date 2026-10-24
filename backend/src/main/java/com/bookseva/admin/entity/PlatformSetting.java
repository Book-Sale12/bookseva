package com.bookseva.admin.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_settings")
public class PlatformSetting {
    @Id
    @Column(name = "setting_key", nullable = false, unique = true)
    private String key;
    @Column(name = "setting_value", nullable = false)
    private String value;
    @Column(columnDefinition = "TEXT")
    private String description;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    public static class PlatformSettingBuilder {
        private String key;
        private String value;
        private String description;
        private LocalDateTime updatedAt;

        PlatformSettingBuilder() {
        }

        /**
         * @return {@code this}.
         */
        public PlatformSetting.PlatformSettingBuilder key(final String key) {
            this.key = key;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public PlatformSetting.PlatformSettingBuilder value(final String value) {
            this.value = value;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public PlatformSetting.PlatformSettingBuilder description(final String description) {
            this.description = description;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public PlatformSetting.PlatformSettingBuilder updatedAt(final LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public PlatformSetting build() {
            return new PlatformSetting(this.key, this.value, this.description, this.updatedAt);
        }

        @Override
        public String toString() {
            return "PlatformSetting.PlatformSettingBuilder(key=" + this.key + ", value=" + this.value + ", description=" + this.description + ", updatedAt=" + this.updatedAt + ")";
        }
    }

    public static PlatformSetting.PlatformSettingBuilder builder() {
        return new PlatformSetting.PlatformSettingBuilder();
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setUpdatedAt(final LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public PlatformSetting() {
    }

    public PlatformSetting(final String key, final String value, final String description, final LocalDateTime updatedAt) {
        this.key = key;
        this.value = value;
        this.description = description;
        this.updatedAt = updatedAt;
    }
}
