package com.example.mybean.order.repository;

import java.util.Optional;
import java.util.UUID;

import com.example.mybean.order.model.Order;
import com.example.mybean.order.model.OrderStatus;

public interface OrderRepository {
	Order insert(Order order);

	int update(UUID orderId, OrderStatus orderStatus); // Order 의 status 를 변경

	int deleteItems(UUID orderId); // 해당 orderId 에 속한 orderItem 들을 제거

	Optional<Order> findById(UUID orderId);  // items 정보를 제외한 Order 정보만을 포함한 Order flxjs

	void deleteAll();
}
