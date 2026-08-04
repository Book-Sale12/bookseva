package com.bookseva.message.controller;

import com.bookseva.message.dto.MessageRequest;
import com.bookseva.message.dto.MessageResponse;
import com.bookseva.message.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<?> sendMessage(
            @Valid @RequestBody MessageRequest request,
            Authentication authentication) {
        
        MessageResponse response = messageService.sendMessage(request, authentication.getName());
        return ResponseEntity.ok(Map.of("success", true, "data", response));
    }

    @GetMapping("/book/{bookId}/user/{userId}")
    public ResponseEntity<?> getConversation(
            @PathVariable Long bookId,
            @PathVariable Long userId,
            Authentication authentication) {
            
        List<MessageResponse> response = messageService.getConversation(bookId, userId, authentication.getName());
        return ResponseEntity.ok(Map.of("success", true, "data", response));
    }

    @GetMapping("/conversations")
    public ResponseEntity<?> getUserConversations(Authentication authentication) {
        return ResponseEntity.ok(Map.of("success", true, "data", messageService.getUserConversations(authentication.getName())));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<?> getUnreadCount(Authentication authentication) {
        return ResponseEntity.ok(Map.of("success", true, "data", messageService.getUnreadCount(authentication.getName())));
    }
}
