package com.bookseva.auth.dto;
import com.bookseva.message.entity.Message;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public class ForgotPasswordRequest {
    @NotBlank
    @Email
    @jakarta.validation.constraints.Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email format")
    private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
