package com.example.mybean.order.dto;

import java.util.List;

import com.example.mybean.order.model.OrderItem;

public record CreateOrderRequest(String email,
								 String address,
								 String postcode,
								 List<OrderItem> orderItems
) {
}
