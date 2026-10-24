package com.bookseva.admin.dto;
import com.bookseva.book.entity.Book;
import com.bookseva.message.entity.Message;


import com.bookseva.book.entity.ListingStatus;

import jakarta.validation.constraints.NotNull;


public class BookStatusUpdateRequest {
    @NotNull(message = "New status is required")
    private ListingStatus newStatus;

	public ListingStatus getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(ListingStatus newStatus) {
		this.newStatus = newStatus;
	}

}
