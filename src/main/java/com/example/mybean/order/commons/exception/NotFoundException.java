package com.example.mybean.order.commons.exception;

public class NotFoundException extends RuntimeException{
	public NotFoundException(String target) {
		super(target + " 이 존재하지 않습니다");
	}
}
