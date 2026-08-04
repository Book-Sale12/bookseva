package com.bookseva.order.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CheckoutRequest {
    @NotEmpty
    private List<Long> cartItemIds;
}
