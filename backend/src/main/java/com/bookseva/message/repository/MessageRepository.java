package com.bookseva.message.repository;

import com.bookseva.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.book.id = :bookId AND " +
           "((m.sender.id = :user1Id AND m.receiver.id = :user2Id) OR " +
           "(m.sender.id = :user2Id AND m.receiver.id = :user1Id)) " +
           "ORDER BY m.createdAt ASC")
    List<Message> findConversation(@Param("bookId") Long bookId, 
                                   @Param("user1Id") Long user1Id, 
                                   @Param("user2Id") Long user2Id);

    @Query("SELECT m FROM Message m WHERE m.sender.email = :email OR m.receiver.email = :email ORDER BY m.createdAt DESC")
    List<Message> findAllUserMessages(@Param("email") String email);

    long countByReceiverIdAndIsReadFalse(Long receiverId);
}
