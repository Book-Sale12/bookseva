package com.bookseva.message.entity;

import com.bookseva.book.entity.Book;
import com.bookseva.user.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    @Column(nullable = false)
    private boolean flagged;
    @Column(name = "is_read", nullable = false)
    private boolean isRead;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    private static boolean $default$flagged() {
        return false;
    }

    private static boolean $default$isRead() {
        return false;
    }


    public static class MessageBuilder {
        private Long id;
        private Book book;
        private User sender;
        private User receiver;
        private String content;
        private boolean flagged$set;
        private boolean flagged$value;
        private boolean isRead$set;
        private boolean isRead$value;
        private LocalDateTime createdAt;

        MessageBuilder() {
        }

        /**
         * @return {@code this}.
         */
        public Message.MessageBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Message.MessageBuilder book(final Book book) {
            this.book = book;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Message.MessageBuilder sender(final User sender) {
            this.sender = sender;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Message.MessageBuilder receiver(final User receiver) {
            this.receiver = receiver;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Message.MessageBuilder content(final String content) {
            this.content = content;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Message.MessageBuilder flagged(final boolean flagged) {
            this.flagged$value = flagged;
            flagged$set = true;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Message.MessageBuilder isRead(final boolean isRead) {
            this.isRead$value = isRead;
            isRead$set = true;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public Message.MessageBuilder createdAt(final LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Message build() {
            boolean flagged$value = this.flagged$value;
            if (!this.flagged$set) flagged$value = Message.$default$flagged();
            boolean isRead$value = this.isRead$value;
            if (!this.isRead$set) isRead$value = Message.$default$isRead();
            return new Message(this.id, this.book, this.sender, this.receiver, this.content, flagged$value, isRead$value, this.createdAt);
        }

        @Override
        public String toString() {
            return "Message.MessageBuilder(id=" + this.id + ", book=" + this.book + ", sender=" + this.sender + ", receiver=" + this.receiver + ", content=" + this.content + ", flagged$value=" + this.flagged$value + ", isRead$value=" + this.isRead$value + ", createdAt=" + this.createdAt + ")";
        }
    }

    public static Message.MessageBuilder builder() {
        return new Message.MessageBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public Book getBook() {
        return this.book;
    }

    public User getSender() {
        return this.sender;
    }

    public User getReceiver() {
        return this.receiver;
    }

    public String getContent() {
        return this.content;
    }

    public boolean isFlagged() {
        return this.flagged;
    }

    public boolean isRead() {
        return this.isRead;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setBook(final Book book) {
        this.book = book;
    }

    public void setSender(final User sender) {
        this.sender = sender;
    }

    public void setReceiver(final User receiver) {
        this.receiver = receiver;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public void setFlagged(final boolean flagged) {
        this.flagged = flagged;
    }

    public void setRead(final boolean isRead) {
        this.isRead = isRead;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Message() {
        this.flagged = Message.$default$flagged();
        this.isRead = Message.$default$isRead();
    }

    public Message(final Long id, final Book book, final User sender, final User receiver, final String content, final boolean flagged, final boolean isRead, final LocalDateTime createdAt) {
        this.id = id;
        this.book = book;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.flagged = flagged;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }
}
