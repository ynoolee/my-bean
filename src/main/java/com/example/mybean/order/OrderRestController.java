package com.example.mybean.order;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.mybean.order.dto.CreateOrderRequest;
import com.example.mybean.order.model.Order;
import com.example.mybean.order.model.OrderStatus;

@RestController
public class OrderRestController {
	private final OrderService orderService;

	public OrderRestController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping("/api/orders")
	public ResponseEntity<Order> newOrder(@RequestBody CreateOrderRequest createOrder) {
		return ResponseEntity
			.ok(orderService
				.createOrder(createOrder.email(),
					createOrder.address(),
					createOrder.postcode(),
					createOrder.orderItems()));
	}

	@PutMapping("/api/orders/{orderId}/cancel")
	public ResponseEntity<?> cancelOrder(@PathVariable UUID orderId, @RequestBody OrderStatus statusRequest) {
		orderService.cancel(orderId);

		return ResponseEntity.ok("주문을 취소하였습니다");
	}

}
