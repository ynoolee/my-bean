package com.example.mybean.order.model;

import java.util.UUID;

public record OrderItem(UUID productId, int quantity) {
}
