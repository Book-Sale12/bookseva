package com.bookseva.message.dto;
import com.bookseva.message.entity.Message;


import java.time.LocalDateTime;

public class ConversationDTO {
    private Long bookId;
    private String bookTitle;
    private String bookImageUrl;
    private Long otherUserId;
    private String otherUserName;
    private String lastMessage;
    private LocalDateTime lastMessageAt;

    ConversationDTO(final Long bookId, final String bookTitle, final String bookImageUrl, final Long otherUserId, final String otherUserName, final String lastMessage, final LocalDateTime lastMessageAt) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookImageUrl = bookImageUrl;
        this.otherUserId = otherUserId;
        this.otherUserName = otherUserName;
        this.lastMessage = lastMessage;
        this.lastMessageAt = lastMessageAt;
    }


    public static class ConversationDTOBuilder {
        private Long bookId;
        private String bookTitle;
        private String bookImageUrl;
        private Long otherUserId;
        private String otherUserName;
        private String lastMessage;
        private LocalDateTime lastMessageAt;

        ConversationDTOBuilder() {
        }

        /**
         * @return {@code this}.
         */
        public ConversationDTO.ConversationDTOBuilder bookId(final Long bookId) {
            this.bookId = bookId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ConversationDTO.ConversationDTOBuilder bookTitle(final String bookTitle) {
            this.bookTitle = bookTitle;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ConversationDTO.ConversationDTOBuilder bookImageUrl(final String bookImageUrl) {
            this.bookImageUrl = bookImageUrl;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ConversationDTO.ConversationDTOBuilder otherUserId(final Long otherUserId) {
            this.otherUserId = otherUserId;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ConversationDTO.ConversationDTOBuilder otherUserName(final String otherUserName) {
            this.otherUserName = otherUserName;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ConversationDTO.ConversationDTOBuilder lastMessage(final String lastMessage) {
            this.lastMessage = lastMessage;
            return this;
        }

        /**
         * @return {@code this}.
         */
        public ConversationDTO.ConversationDTOBuilder lastMessageAt(final LocalDateTime lastMessageAt) {
            this.lastMessageAt = lastMessageAt;
            return this;
        }

        public ConversationDTO build() {
            return new ConversationDTO(this.bookId, this.bookTitle, this.bookImageUrl, this.otherUserId, this.otherUserName, this.lastMessage, this.lastMessageAt);
        }

        @Override
        public String toString() {
            return "ConversationDTO.ConversationDTOBuilder(bookId=" + this.bookId + ", bookTitle=" + this.bookTitle + ", bookImageUrl=" + this.bookImageUrl + ", otherUserId=" + this.otherUserId + ", otherUserName=" + this.otherUserName + ", lastMessage=" + this.lastMessage + ", lastMessageAt=" + this.lastMessageAt + ")";
        }
    }

    public static ConversationDTO.ConversationDTOBuilder builder() {
        return new ConversationDTO.ConversationDTOBuilder();
    }

    public Long getBookId() {
        return this.bookId;
    }

    public String getBookTitle() {
        return this.bookTitle;
    }

    public String getBookImageUrl() {
        return this.bookImageUrl;
    }

    public Long getOtherUserId() {
        return this.otherUserId;
    }

    public String getOtherUserName() {
        return this.otherUserName;
    }

    public String getLastMessage() {
        return this.lastMessage;
    }

    public LocalDateTime getLastMessageAt() {
        return this.lastMessageAt;
    }

    public void setBookId(final Long bookId) {
        this.bookId = bookId;
    }

    public void setBookTitle(final String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setBookImageUrl(final String bookImageUrl) {
        this.bookImageUrl = bookImageUrl;
    }

    public void setOtherUserId(final Long otherUserId) {
        this.otherUserId = otherUserId;
    }

    public void setOtherUserName(final String otherUserName) {
        this.otherUserName = otherUserName;
    }

    public void setLastMessage(final String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setLastMessageAt(final LocalDateTime lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ConversationDTO)) return false;
        final ConversationDTO other = (ConversationDTO) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$bookId = this.getBookId();
        final Object other$bookId = other.getBookId();
        if (this$bookId == null ? other$bookId != null : !this$bookId.equals(other$bookId)) return false;
        final Object this$otherUserId = this.getOtherUserId();
        final Object other$otherUserId = other.getOtherUserId();
        if (this$otherUserId == null ? other$otherUserId != null : !this$otherUserId.equals(other$otherUserId)) return false;
        final Object this$bookTitle = this.getBookTitle();
        final Object other$bookTitle = other.getBookTitle();
        if (this$bookTitle == null ? other$bookTitle != null : !this$bookTitle.equals(other$bookTitle)) return false;
        final Object this$bookImageUrl = this.getBookImageUrl();
        final Object other$bookImageUrl = other.getBookImageUrl();
        if (this$bookImageUrl == null ? other$bookImageUrl != null : !this$bookImageUrl.equals(other$bookImageUrl)) return false;
        final Object this$otherUserName = this.getOtherUserName();
        final Object other$otherUserName = other.getOtherUserName();
        if (this$otherUserName == null ? other$otherUserName != null : !this$otherUserName.equals(other$otherUserName)) return false;
        final Object this$lastMessage = this.getLastMessage();
        final Object other$lastMessage = other.getLastMessage();
        if (this$lastMessage == null ? other$lastMessage != null : !this$lastMessage.equals(other$lastMessage)) return false;
        final Object this$lastMessageAt = this.getLastMessageAt();
        final Object other$lastMessageAt = other.getLastMessageAt();
        if (this$lastMessageAt == null ? other$lastMessageAt != null : !this$lastMessageAt.equals(other$lastMessageAt)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ConversationDTO;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $bookId = this.getBookId();
        result = result * PRIME + ($bookId == null ? 43 : $bookId.hashCode());
        final Object $otherUserId = this.getOtherUserId();
        result = result * PRIME + ($otherUserId == null ? 43 : $otherUserId.hashCode());
        final Object $bookTitle = this.getBookTitle();
        result = result * PRIME + ($bookTitle == null ? 43 : $bookTitle.hashCode());
        final Object $bookImageUrl = this.getBookImageUrl();
        result = result * PRIME + ($bookImageUrl == null ? 43 : $bookImageUrl.hashCode());
        final Object $otherUserName = this.getOtherUserName();
        result = result * PRIME + ($otherUserName == null ? 43 : $otherUserName.hashCode());
        final Object $lastMessage = this.getLastMessage();
        result = result * PRIME + ($lastMessage == null ? 43 : $lastMessage.hashCode());
        final Object $lastMessageAt = this.getLastMessageAt();
        result = result * PRIME + ($lastMessageAt == null ? 43 : $lastMessageAt.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "ConversationDTO(bookId=" + this.getBookId() + ", bookTitle=" + this.getBookTitle() + ", bookImageUrl=" + this.getBookImageUrl() + ", otherUserId=" + this.getOtherUserId() + ", otherUserName=" + this.getOtherUserName() + ", lastMessage=" + this.getLastMessage() + ", lastMessageAt=" + this.getLastMessageAt() + ")";
    }
}
