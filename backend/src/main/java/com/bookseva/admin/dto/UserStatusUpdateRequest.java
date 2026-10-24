package com.bookseva.admin.dto;
import com.bookseva.message.entity.Message;
import com.bookseva.user.entity.User;


import com.bookseva.user.entity.UserStatus;

import jakarta.validation.constraints.NotNull;


public class UserStatusUpdateRequest {
    @NotNull(message = "New status is required")
    private UserStatus newStatus;

	public UserStatus getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(UserStatus newStatus) {
		this.newStatus = newStatus;
	}
}
