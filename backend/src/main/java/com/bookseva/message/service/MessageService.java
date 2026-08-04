package com.bookseva.message.service;

import com.bookseva.book.Book;
import com.bookseva.book.BookRepository;
import com.bookseva.message.Message;
import com.bookseva.message.dto.ConversationDTO;
import com.bookseva.message.dto.MessageRequest;
import com.bookseva.message.dto.MessageResponse;
import com.bookseva.message.repository.MessageRepository;
import com.bookseva.user.User;
import com.bookseva.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public MessageResponse sendMessage(MessageRequest request, String senderEmail) {
        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
                
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));
                
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (sender.getId().equals(receiver.getId())) {
            throw new RuntimeException("You cannot send a message to yourself");
        }
        if (!book.getSeller().getId().equals(sender.getId()) && !book.getSeller().getId().equals(receiver.getId())) {
            throw new RuntimeException("Unauthorized communication channel for this book");
        }

        Message message = Message.builder()
                .book(book)
                .sender(sender)
                .receiver(receiver)
                .content(request.getContent())
                .flagged(false)
                .build();
                
        message = messageRepository.save(message);
        
        return mapToResponse(message);
    }

    @Transactional
    public List<MessageResponse> getConversation(Long bookId, Long user1Id, String currentUserEmail) {
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
                
        List<Message> messages = messageRepository.findConversation(bookId, currentUser.getId(), user1Id);
        
        boolean updated = false;
        for (Message m : messages) {
            if (!m.isRead() && m.getReceiver().getId().equals(currentUser.getId())) {
                m.setRead(true);
                updated = true;
            }
        }
        if (updated) {
            messageRepository.saveAll(messages);
        }
        
        return messages.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public long getUnreadCount(String currentUserEmail) {
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return messageRepository.countByReceiverIdAndIsReadFalse(currentUser.getId());
    }

    public List<ConversationDTO> getUserConversations(String currentUserEmail) {
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
                
        List<Message> allMessages = messageRepository.findAllUserMessages(currentUserEmail);
        
        // Group by unique key (bookId_otherUserId) keeping the latest message
        Map<String, Message> latestMessagePerConversation = new LinkedHashMap<>();
        
        for (Message m : allMessages) {
            Long otherUserId = m.getSender().getId().equals(currentUser.getId()) ? m.getReceiver().getId() : m.getSender().getId();
            String key = m.getBook().getId() + "_" + otherUserId;
            
            latestMessagePerConversation.putIfAbsent(key, m); // Since list is ordered DESC, first seen is latest
        }
        
        return latestMessagePerConversation.values().stream().map(m -> {
            boolean amISender = m.getSender().getId().equals(currentUser.getId());
            Long otherUserId = amISender ? m.getReceiver().getId() : m.getSender().getId();
            String otherUserName = amISender ? m.getReceiver().getName() : m.getSender().getName();
            
            String imgUrl = m.getBook().getImages().isEmpty() ? null : m.getBook().getImages().get(0).getUrl();
            
            return ConversationDTO.builder()
                .bookId(m.getBook().getId())
                .bookTitle(m.getBook().getTitle())
                .bookImageUrl(imgUrl)
                .otherUserId(otherUserId)
                .otherUserName(otherUserName)
                .lastMessage(m.getContent())
                .lastMessageAt(m.getCreatedAt())
                .build();
        }).collect(Collectors.toList());
    }

    private MessageResponse mapToResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .senderId(message.getSender().getId())
                .senderName(message.getSender().getName())
                .receiverId(message.getReceiver().getId())
                .receiverName(message.getReceiver().getName())
                .bookId(message.getBook().getId())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
