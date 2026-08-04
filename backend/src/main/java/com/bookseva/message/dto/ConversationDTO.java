package com.bookseva.message.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ConversationDTO {
    private Long bookId;
    private String bookTitle;
    private String bookImageUrl;
    private Long otherUserId;
    private String otherUserName;
    private String lastMessage;
    private LocalDateTime lastMessageAt;
}
