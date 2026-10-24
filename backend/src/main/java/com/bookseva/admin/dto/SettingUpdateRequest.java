package com.bookseva.admin.dto;
import com.bookseva.message.entity.Message;


import jakarta.validation.constraints.NotBlank;


public class SettingUpdateRequest {
    @NotBlank(message = "New value is required")
    private String newValue;

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
}
