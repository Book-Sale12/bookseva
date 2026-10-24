package com.bookseva.message.dto;
import com.bookseva.message.entity.Message;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MessageRequest {
    @NotNull
    private Long bookId;
    @NotNull
    private Long receiverId;
    @NotBlank
    private String content;

    public MessageRequest() {
    }

    public Long getBookId() {
        return this.bookId;
    }

    public Long getReceiverId() {
        return this.receiverId;
    }

    public String getContent() {
        return this.content;
    }

    public void setBookId(final Long bookId) {
        this.bookId = bookId;
    }

    public void setReceiverId(final Long receiverId) {
        this.receiverId = receiverId;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof MessageRequest)) return false;
        final MessageRequest other = (MessageRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$bookId = this.getBookId();
        final Object other$bookId = other.getBookId();
        if (this$bookId == null ? other$bookId != null : !this$bookId.equals(other$bookId)) return false;
        final Object this$receiverId = this.getReceiverId();
        final Object other$receiverId = other.getReceiverId();
        if (this$receiverId == null ? other$receiverId != null : !this$receiverId.equals(other$receiverId)) return false;
        final Object this$content = this.getContent();
        final Object other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof MessageRequest;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $bookId = this.getBookId();
        result = result * PRIME + ($bookId == null ? 43 : $bookId.hashCode());
        final Object $receiverId = this.getReceiverId();
        result = result * PRIME + ($receiverId == null ? 43 : $receiverId.hashCode());
        final Object $content = this.getContent();
        result = result * PRIME + ($content == null ? 43 : $content.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "MessageRequest(bookId=" + this.getBookId() + ", receiverId=" + this.getReceiverId() + ", content=" + this.getContent() + ")";
    }
}
