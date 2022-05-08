package com.example.mybean.order;

import java.util.Arrays;

import com.example.mybean.commons.exception.NotFoundException;

public enum OrderStatus {
	ACCEPTED,
	PAYMENT_CONFIRMED,
	READY_FOR_DELIVERY,
	SHIPPED,
	SETTLED,
	CANCELED;

	public static OrderStatus of(String status) {
		return Arrays.stream(OrderStatus.values())
			.filter(s ->
				s.name().equalsIgnoreCase(status))
			.findFirst()
			.orElseThrow(()->
				new NotFoundException(status));
	}
}
