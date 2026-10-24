package com.bookseva.order.dto;
import com.bookseva.order.entity.Order;


import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class CheckoutRequest {
    @NotEmpty
    private List<Long> cartItemIds;

    public CheckoutRequest() {
    }

    public List<Long> getCartItemIds() {
        return this.cartItemIds;
    }

    public void setCartItemIds(final List<Long> cartItemIds) {
        this.cartItemIds = cartItemIds;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof CheckoutRequest)) return false;
        final CheckoutRequest other = (CheckoutRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$cartItemIds = this.getCartItemIds();
        final Object other$cartItemIds = other.getCartItemIds();
        if (this$cartItemIds == null ? other$cartItemIds != null : !this$cartItemIds.equals(other$cartItemIds)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CheckoutRequest;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $cartItemIds = this.getCartItemIds();
        result = result * PRIME + ($cartItemIds == null ? 43 : $cartItemIds.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "CheckoutRequest(cartItemIds=" + this.getCartItemIds() + ")";
    }
}
