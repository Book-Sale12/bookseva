package com.bookseva.message.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageRequest {
    @NotNull
    private Long bookId;
    @NotNull
    private Long receiverId;
    @NotBlank
    private String content;
}
