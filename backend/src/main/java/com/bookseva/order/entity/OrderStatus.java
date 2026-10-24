package com.bookseva.order.entity;

public enum OrderStatus {
    PLACED,
    PAYMENT_PENDING,
    PAID,
    CONFIRMED_BY_SELLER,
    HANDED_OVER,
    SHIPPED,
    COMPLETED,
    CANCELLED,
    DISPUTED,
    REFUNDED
}
