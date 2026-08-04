package com.bookseva.admin.dto;

import com.bookseva.user.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserStatusUpdateRequest {
    @NotNull(message = "New status is required")
    private UserStatus newStatus;
}
