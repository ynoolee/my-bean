package com.example.mybean.product;

import java.time.LocalDateTime;
import java.util.UUID;

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
		checkNull(productId, "id 는 null 이 올 수 없습니다");
		checkEmpty(productName, "productName 은 빈 칸이 올 수 없습니다");
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

	public <T> void checkNull(T obj, String message) {
		if (obj == null) {
			throw new IllegalArgumentException(message);
		}
	}

	public void checkEmpty(CharSequence str, String message) {
		if (str == null || str.isEmpty()) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void checkPositiveOrZero(long number, String message) {
		if (number < 0) {
			throw new IllegalArgumentException(message);
		}
	}

	private void hasEnoughStock(int quantity) {
		if (this.stock < quantity) {
			throw new StockOutException();
		}
	}

	private boolean isPositiveOrZero(int quantity) {
		return quantity >= 0;
	}

	public void decreaseStock(int quantity) throws StockOutException{
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

	public void setProductName(String productName) {
		this.productName = productName;
		this.updatedAt = LocalDateTime.now();
	}

	public void setPrice(long price) {
		this.price = price;
		this.updatedAt = LocalDateTime.now();
	}

	public void setDescription(String description) {
		this.description = description;
		this.updatedAt = LocalDateTime.now();
	}
}
