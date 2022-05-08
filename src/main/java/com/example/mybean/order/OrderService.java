package com.example.mybean.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mybean.order.model.Email;
import com.example.mybean.order.model.Order;
import com.example.mybean.order.model.OrderItem;
import com.example.mybean.order.model.OrderStatus;
import com.example.mybean.order.repository.OrderRepository;
import com.example.mybean.product.ProductService;

@Service
public class OrderService {
	private final OrderRepository orderRepository;
	private final ProductService productService;

	public OrderService(OrderRepository orderRepository, ProductService productService) {
		this.orderRepository = orderRepository;
		this.productService = productService;
	}

	@Transactional
	public Order createOrder(String email, String address, String postcode, List<OrderItem> orderItems) {
		for (OrderItem item : orderItems) {
			productService.sell(item.productId(), item.quantity());
		}

		Order order = new Order(UUID.randomUUID(),
			Email.of(email),
			address,
			postcode,
			orderItems,
			OrderStatus.ACCEPTED,
			LocalDateTime.now(),
			LocalDateTime.now());

		return orderRepository.insert(order);
	}

	@Transactional
	public void cancel(UUID orderId) {
		// order 취소
		orderRepository.update(orderId, OrderStatus.CANCELED);

		// orderItem 들 삭제
		orderRepository.deleteItems(orderId);
	}
}
