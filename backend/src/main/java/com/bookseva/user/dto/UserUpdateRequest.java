package com.bookseva.user.dto;
import com.bookseva.message.entity.Message;
import com.bookseva.user.entity.User;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserUpdateRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be at least 2 characters")
    private String name;
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Indian mobile number format")
    private String phone;
    @NotBlank(message = "College Name is required")
    @Size(min = 2, max = 100, message = "College name must be at least 2 characters")
    private String collegeName;
    @NotBlank(message = "Course/Branch is required")
    @Size(min = 2, max = 100, message = "Course/Branch must be at least 2 characters")
    private String courseBranch;

    public UserUpdateRequest() {
    }

    public String getName() {
        return this.name;
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

    public void setName(final String name) {
        this.name = name;
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

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof UserUpdateRequest)) return false;
        final UserUpdateRequest other = (UserUpdateRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$phone = this.getPhone();
        final Object other$phone = other.getPhone();
        if (this$phone == null ? other$phone != null : !this$phone.equals(other$phone)) return false;
        final Object this$collegeName = this.getCollegeName();
        final Object other$collegeName = other.getCollegeName();
        if (this$collegeName == null ? other$collegeName != null : !this$collegeName.equals(other$collegeName)) return false;
        final Object this$courseBranch = this.getCourseBranch();
        final Object other$courseBranch = other.getCourseBranch();
        if (this$courseBranch == null ? other$courseBranch != null : !this$courseBranch.equals(other$courseBranch)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof UserUpdateRequest;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $phone = this.getPhone();
        result = result * PRIME + ($phone == null ? 43 : $phone.hashCode());
        final Object $collegeName = this.getCollegeName();
        result = result * PRIME + ($collegeName == null ? 43 : $collegeName.hashCode());
        final Object $courseBranch = this.getCourseBranch();
        result = result * PRIME + ($courseBranch == null ? 43 : $courseBranch.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "UserUpdateRequest(name=" + this.getName() + ", phone=" + this.getPhone() + ", collegeName=" + this.getCollegeName() + ", courseBranch=" + this.getCourseBranch() + ")";
    }
}
