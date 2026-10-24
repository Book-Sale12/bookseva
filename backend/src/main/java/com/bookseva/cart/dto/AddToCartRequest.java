package com.bookseva.cart.dto;
import com.bookseva.cart.entity.Cart;


import jakarta.validation.constraints.NotNull;

public class AddToCartRequest {
    @NotNull
    private Long bookId;

    public AddToCartRequest() {
    }

    public Long getBookId() {
        return this.bookId;
    }

    public void setBookId(final Long bookId) {
        this.bookId = bookId;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof AddToCartRequest)) return false;
        final AddToCartRequest other = (AddToCartRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$bookId = this.getBookId();
        final Object other$bookId = other.getBookId();
        if (this$bookId == null ? other$bookId != null : !this$bookId.equals(other$bookId)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof AddToCartRequest;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $bookId = this.getBookId();
        result = result * PRIME + ($bookId == null ? 43 : $bookId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "AddToCartRequest(bookId=" + this.getBookId() + ")";
    }
}
