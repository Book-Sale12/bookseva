package com.bookseva.auth.dto;

import com.bookseva.common.validation.ValidCollegeName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Name must not be empty")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Name must not contain numbers or special characters")
    private String name;

    @NotBlank(message = "Email must not be empty")
    @Email(message = "Invalid email format")
    @Pattern(
        regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
        message = "Invalid email format"
    )
    private String email;

    @NotBlank(message = "Password must not be empty")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$",
        message = "Password must be at least 8 characters, with one uppercase, one lowercase, one number, and one special character"
    )
    private String password;

    @NotBlank(message = "Phone number must not be empty")
    @Pattern(
        regexp = "^\\d{10}$",
        message = "Phone number must be exactly 10 digits."
    )
    private String phone;

    @NotBlank(message = "College name must not be empty")
    @ValidCollegeName
    private String collegeName;

    @NotBlank(message = "Course/Branch must not be empty")
    private String courseBranch;
}
