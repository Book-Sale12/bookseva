package com.bookseva.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResendOtpRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;
}
