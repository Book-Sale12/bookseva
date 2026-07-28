package com.bookseva.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
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
}
