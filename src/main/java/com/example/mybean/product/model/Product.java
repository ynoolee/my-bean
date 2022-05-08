package com.example.mybean.product.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.util.Assert;

import com.example.mybean.product.exception.StockOutException;

public class Product {
	private final UUID productId;
	private String productName;
	private long price;
	private int stock;
	private String description;
	private final LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public Product(UUID productId, String productName, long price, int stock, String description,
		LocalDateTime createdAt, LocalDateTime updatedAt) {
		Assert.notNull(productId, "id 는 null 이 올 수 없습니다");
		Assert.hasLength(productName, "productName 은 비어있는 문자열이면 안됩니다");
		checkPositiveOrZero(price, "price 는 0 이상이어야 합니다");
		checkPositiveOrZero(stock, "stock 는 0 이상이어야 합니다");

		this.productId = productId;
		this.productName = productName;
		this.price = price;
		this.stock = stock;
		this.description = description;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	private void checkPositiveOrZero(long number, String message) {
		if (number < 0) {
			throw new IllegalArgumentException(message);
		}
	}

	private void hasEnoughStock(int quantity) {
		if (this.stock < quantity) {
			throw new StockOutException(this.productId.toString());
		}
	}

	private boolean isPositiveOrZero(int quantity) {
		return quantity >= 0;
	}

	public void decreaseStock(int quantity) throws StockOutException {
		if (isPositiveOrZero(quantity)) {
			this.hasEnoughStock(quantity);

			this.stock -= quantity;
			this.updatedAt = LocalDateTime.now();
		}
	}

	public UUID getProductId() {
		return productId;
	}

	public String getProductName() {
		return productName;
	}

	public long getPrice() {
		return price;
	}

	public int getStock() {
		return stock;
	}

	public String getDescription() {
		return description;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void changeProductName(String productName) {
		Assert.hasLength(productName, "productName 은 비어있는 문자열이면 안됩니다");

		this.productName = productName;
		this.updatedAt = LocalDateTime.now();
	}

	public void changePrice(long price) {
		this.checkPositiveOrZero(price, "price 는 0 이상이어야 합니다");

		this.price = price;
		this.updatedAt = LocalDateTime.now();
	}

	public void changeDescription(String description) {
		Assert.hasLength(description, "description 은 비어있는 문자열이면 안됩니다");

		this.description = description;
		this.updatedAt = LocalDateTime.now();
	}
}
