package com.example.mybean.product.dto;

public record CreateProductRequest(String productName,
								   long price,
								   int stock,
								   String description) {
}
