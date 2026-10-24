package com.bookseva.user.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @com.fasterxml.jackson.annotation.JsonIgnore
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    @Column(nullable = false)
    private String phone;
    @Column(name = "college_name", nullable = false)
    private String collegeName;
    @Column(name = "course_branch", nullable = false)
    private String courseBranch;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;
    @Column(name = "trust_score", nullable = false)
    private Integer trustScore;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    private static Integer $default$trustScore() {
        return 100;
    }


    public static class UserBuilder {
        private Long id;
        private String name;
        private String email;
        private String passwordHash;
        private String phone;
        private String collegeName;
        private String courseBranch;
        private Role role;
        private UserStatus status;
        private boolean trustScore$set;
        private Integer trustScore$value;
        private LocalDateTime createdAt;

        UserBuilder() {
        }

        /**
         * @return {@code this}.
         */
        public User.UserBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public User.UserBuilder name(final String name) {
            this.name = name;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public User.UserBuilder email(final String email) {
            this.email = email;
            return this;
        }

        /**
         * @return {@code this}.
         */
        @com.fasterxml.jackson.annotation.JsonIgnore
        public User.UserBuilder passwordHash(final String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public User.UserBuilder phone(final String phone) {
            this.phone = phone;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public User.UserBuilder collegeName(final String collegeName) {
            this.collegeName = collegeName;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public User.UserBuilder courseBranch(final String courseBranch) {
            this.courseBranch = courseBranch;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public User.UserBuilder role(final Role role) {
            this.role = role;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public User.UserBuilder status(final UserStatus status) {
            this.status = status;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public User.UserBuilder trustScore(final Integer trustScore) {
            this.trustScore$value = trustScore;
            trustScore$set = true;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public User.UserBuilder createdAt(final LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public User build() {
            Integer trustScore$value = this.trustScore$value;
            if (!this.trustScore$set) trustScore$value = User.$default$trustScore();
            return new User(this.id, this.name, this.email, this.passwordHash, this.phone, this.collegeName, this.courseBranch, this.role, this.status, trustScore$value, this.createdAt);
        }

        @Override
        public String toString() {
            return "User.UserBuilder(id=" + this.id + ", name=" + this.name + ", email=" + this.email + ", passwordHash=" + this.passwordHash + ", phone=" + this.phone + ", collegeName=" + this.collegeName + ", courseBranch=" + this.courseBranch + ", role=" + this.role + ", status=" + this.status + ", trustScore$value=" + this.trustScore$value + ", createdAt=" + this.createdAt + ")";
        }
    }

    public static User.UserBuilder builder() {
        return new User.UserBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getCollegeName() {
        return this.collegeName;
    }

    public String getCourseBranch() {
        return this.courseBranch;
    }

    public Role getRole() {
        return this.role;
    }

    public UserStatus getStatus() {
        return this.status;
    }

    public Integer getTrustScore() {
        return this.trustScore;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setPasswordHash(final String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public void setCollegeName(final String collegeName) {
        this.collegeName = collegeName;
    }

    public void setCourseBranch(final String courseBranch) {
        this.courseBranch = courseBranch;
    }

    public void setRole(final Role role) {
        this.role = role;
    }

    public void setStatus(final UserStatus status) {
        this.status = status;
    }

    public void setTrustScore(final Integer trustScore) {
        this.trustScore = trustScore;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof User)) return false;
        final User other = (User) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$trustScore = this.getTrustScore();
        final Object other$trustScore = other.getTrustScore();
        if (this$trustScore == null ? other$trustScore != null : !this$trustScore.equals(other$trustScore)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$email = this.getEmail();
        final Object other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) return false;
        final Object this$passwordHash = this.getPasswordHash();
        final Object other$passwordHash = other.getPasswordHash();
        if (this$passwordHash == null ? other$passwordHash != null : !this$passwordHash.equals(other$passwordHash)) return false;
        final Object this$phone = this.getPhone();
        final Object other$phone = other.getPhone();
        if (this$phone == null ? other$phone != null : !this$phone.equals(other$phone)) return false;
        final Object this$collegeName = this.getCollegeName();
        final Object other$collegeName = other.getCollegeName();
        if (this$collegeName == null ? other$collegeName != null : !this$collegeName.equals(other$collegeName)) return false;
        final Object this$courseBranch = this.getCourseBranch();
        final Object other$courseBranch = other.getCourseBranch();
        if (this$courseBranch == null ? other$courseBranch != null : !this$courseBranch.equals(other$courseBranch)) return false;
        final Object this$role = this.getRole();
        final Object other$role = other.getRole();
        if (this$role == null ? other$role != null : !this$role.equals(other$role)) return false;
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
        final Object this$createdAt = this.getCreatedAt();
        final Object other$createdAt = other.getCreatedAt();
        if (this$createdAt == null ? other$createdAt != null : !this$createdAt.equals(other$createdAt)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof User;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $trustScore = this.getTrustScore();
        result = result * PRIME + ($trustScore == null ? 43 : $trustScore.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $email = this.getEmail();
        result = result * PRIME + ($email == null ? 43 : $email.hashCode());
        final Object $passwordHash = this.getPasswordHash();
        result = result * PRIME + ($passwordHash == null ? 43 : $passwordHash.hashCode());
        final Object $phone = this.getPhone();
        result = result * PRIME + ($phone == null ? 43 : $phone.hashCode());
        final Object $collegeName = this.getCollegeName();
        result = result * PRIME + ($collegeName == null ? 43 : $collegeName.hashCode());
        final Object $courseBranch = this.getCourseBranch();
        result = result * PRIME + ($courseBranch == null ? 43 : $courseBranch.hashCode());
        final Object $role = this.getRole();
        result = result * PRIME + ($role == null ? 43 : $role.hashCode());
        final Object $status = this.getStatus();
        result = result * PRIME + ($status == null ? 43 : $status.hashCode());
        final Object $createdAt = this.getCreatedAt();
        result = result * PRIME + ($createdAt == null ? 43 : $createdAt.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "User(id=" + this.getId() + ", name=" + this.getName() + ", email=" + this.getEmail() + ", passwordHash=" + this.getPasswordHash() + ", phone=" + this.getPhone() + ", collegeName=" + this.getCollegeName() + ", courseBranch=" + this.getCourseBranch() + ", role=" + this.getRole() + ", status=" + this.getStatus() + ", trustScore=" + this.getTrustScore() + ", createdAt=" + this.getCreatedAt() + ")";
    }

    public User() {
        this.trustScore = User.$default$trustScore();
    }

    public User(final Long id, final String name, final String email, final String passwordHash, final String phone, final String collegeName, final String courseBranch, final Role role, final UserStatus status, final Integer trustScore, final LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phone = phone;
        this.collegeName = collegeName;
        this.courseBranch = courseBranch;
        this.role = role;
        this.status = status;
        this.trustScore = trustScore;
        this.createdAt = createdAt;
    }
}
