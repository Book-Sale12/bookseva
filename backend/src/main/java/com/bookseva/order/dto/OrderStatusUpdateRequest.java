package com.bookseva.order.dto;
import com.bookseva.message.entity.Message;
import com.bookseva.order.entity.Order;


import com.bookseva.order.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

public class OrderStatusUpdateRequest {
    @NotNull(message = "New status is required")
    private OrderStatus newStatus;

    public OrderStatus getNewStatus() {
        return this.newStatus;
    }

    public void setNewStatus(final OrderStatus newStatus) {
        this.newStatus = newStatus;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof OrderStatusUpdateRequest)) return false;
        final OrderStatusUpdateRequest other = (OrderStatusUpdateRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$newStatus = this.getNewStatus();
        final Object other$newStatus = other.getNewStatus();
        if (this$newStatus == null ? other$newStatus != null : !this$newStatus.equals(other$newStatus)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof OrderStatusUpdateRequest;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $newStatus = this.getNewStatus();
        result = result * PRIME + ($newStatus == null ? 43 : $newStatus.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "OrderStatusUpdateRequest(newStatus=" + this.getNewStatus() + ")";
    }

    public OrderStatusUpdateRequest() {
    }

    public OrderStatusUpdateRequest(final OrderStatus newStatus) {
        this.newStatus = newStatus;
    }
}
