package com.example.mybean.product.exception;

public class StockOutException extends RuntimeException{
	public StockOutException(String targetId) {
		super(targetId + ": 충분한 재고가 없습니다");
	}
}
