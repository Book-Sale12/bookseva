package com.bookseva.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SettingUpdateRequest {
    @NotBlank(message = "New value is required")
    private String newValue;
}
