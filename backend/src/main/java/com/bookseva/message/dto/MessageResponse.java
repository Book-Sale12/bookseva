package com.bookseva.message.dto;
import com.bookseva.message.entity.Message;


import java.time.LocalDateTime;

public class MessageResponse {
    private Long id;
    private Long senderId;
    private String senderName;
    private Long receiverId;
    private String receiverName;
    private Long bookId;
    private String content;
    private LocalDateTime createdAt;

    MessageResponse(final Long id, final Long senderId, final String senderName, final Long receiverId, final String receiverName, final Long bookId, final String content, final LocalDateTime createdAt) {
        this.id = id;
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.bookId = bookId;
        this.content = content;
        this.createdAt = createdAt;
    }


    public static class MessageResponseBuilder {
        private Long id;
        private Long senderId;
        private String senderName;
        private Long receiverId;
        private String receiverName;
        private Long bookId;
        private String content;
        private LocalDateTime createdAt;

        MessageResponseBuilder() {
        }

        /**
         * @return {@code this}.
         */
        public MessageResponse.MessageResponseBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public MessageResponse.MessageResponseBuilder senderId(final Long senderId) {
            this.senderId = senderId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public MessageResponse.MessageResponseBuilder senderName(final String senderName) {
            this.senderName = senderName;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public MessageResponse.MessageResponseBuilder receiverId(final Long receiverId) {
            this.receiverId = receiverId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public MessageResponse.MessageResponseBuilder receiverName(final String receiverName) {
            this.receiverName = receiverName;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public MessageResponse.MessageResponseBuilder bookId(final Long bookId) {
            this.bookId = bookId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public MessageResponse.MessageResponseBuilder content(final String content) {
            this.content = content;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public MessageResponse.MessageResponseBuilder createdAt(final LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public MessageResponse build() {
            return new MessageResponse(this.id, this.senderId, this.senderName, this.receiverId, this.receiverName, this.bookId, this.content, this.createdAt);
        }

        @Override
        public String toString() {
            return "MessageResponse.MessageResponseBuilder(id=" + this.id + ", senderId=" + this.senderId + ", senderName=" + this.senderName + ", receiverId=" + this.receiverId + ", receiverName=" + this.receiverName + ", bookId=" + this.bookId + ", content=" + this.content + ", createdAt=" + this.createdAt + ")";
        }
    }

    public static MessageResponse.MessageResponseBuilder builder() {
        return new MessageResponse.MessageResponseBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public Long getSenderId() {
        return this.senderId;
    }

    public String getSenderName() {
        return this.senderName;
    }

    public Long getReceiverId() {
        return this.receiverId;
    }

    public String getReceiverName() {
        return this.receiverName;
    }

    public Long getBookId() {
        return this.bookId;
    }

    public String getContent() {
        return this.content;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setSenderId(final Long senderId) {
        this.senderId = senderId;
    }

    public void setSenderName(final String senderName) {
        this.senderName = senderName;
    }

    public void setReceiverId(final Long receiverId) {
        this.receiverId = receiverId;
    }

    public void setReceiverName(final String receiverName) {
        this.receiverName = receiverName;
    }

    public void setBookId(final Long bookId) {
        this.bookId = bookId;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof MessageResponse)) return false;
        final MessageResponse other = (MessageResponse) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$senderId = this.getSenderId();
        final Object other$senderId = other.getSenderId();
        if (this$senderId == null ? other$senderId != null : !this$senderId.equals(other$senderId)) return false;
        final Object this$receiverId = this.getReceiverId();
        final Object other$receiverId = other.getReceiverId();
        if (this$receiverId == null ? other$receiverId != null : !this$receiverId.equals(other$receiverId)) return false;
        final Object this$bookId = this.getBookId();
        final Object other$bookId = other.getBookId();
        if (this$bookId == null ? other$bookId != null : !this$bookId.equals(other$bookId)) return false;
        final Object this$senderName = this.getSenderName();
        final Object other$senderName = other.getSenderName();
        if (this$senderName == null ? other$senderName != null : !this$senderName.equals(other$senderName)) return false;
        final Object this$receiverName = this.getReceiverName();
        final Object other$receiverName = other.getReceiverName();
        if (this$receiverName == null ? other$receiverName != null : !this$receiverName.equals(other$receiverName)) return false;
        final Object this$content = this.getContent();
        final Object other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content)) return false;
        final Object this$createdAt = this.getCreatedAt();
        final Object other$createdAt = other.getCreatedAt();
        if (this$createdAt == null ? other$createdAt != null : !this$createdAt.equals(other$createdAt)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof MessageResponse;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $senderId = this.getSenderId();
        result = result * PRIME + ($senderId == null ? 43 : $senderId.hashCode());
        final Object $receiverId = this.getReceiverId();
        result = result * PRIME + ($receiverId == null ? 43 : $receiverId.hashCode());
        final Object $bookId = this.getBookId();
        result = result * PRIME + ($bookId == null ? 43 : $bookId.hashCode());
        final Object $senderName = this.getSenderName();
        result = result * PRIME + ($senderName == null ? 43 : $senderName.hashCode());
        final Object $receiverName = this.getReceiverName();
        result = result * PRIME + ($receiverName == null ? 43 : $receiverName.hashCode());
        final Object $content = this.getContent();
        result = result * PRIME + ($content == null ? 43 : $content.hashCode());
        final Object $createdAt = this.getCreatedAt();
        result = result * PRIME + ($createdAt == null ? 43 : $createdAt.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "MessageResponse(id=" + this.getId() + ", senderId=" + this.getSenderId() + ", senderName=" + this.getSenderName() + ", receiverId=" + this.getReceiverId() + ", receiverName=" + this.getReceiverName() + ", bookId=" + this.getBookId() + ", content=" + this.getContent() + ", createdAt=" + this.getCreatedAt() + ")";
    }
}
