package com.bookseva.auth.dto;
import com.bookseva.message.entity.Message;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public class ResendOtpRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
