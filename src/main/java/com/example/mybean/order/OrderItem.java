package com.example.mybean.order;

import java.util.UUID;

public record OrderItem(UUID productId, long price, int quantity) {
}
