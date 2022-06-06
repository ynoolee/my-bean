package com.example.mybean.order.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.mybean.order.model.Email;
import com.example.mybean.order.model.Order;
import com.example.mybean.order.model.OrderItem;
import com.example.mybean.order.model.OrderStatus;
import com.example.mybean.product.model.Product;
import com.example.mybean.product.repository.ProductRepository;

@SpringBootTest
@ActiveProfiles("test")
class OrderRepositoryTest {
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	ProductRepository productRepository;

	private Product kenya = new Product(UUID.randomUUID(), "Guata-bean",
		10000L, 100, null, LocalDateTime.now(), LocalDateTime.now());
	private Product america = new Product(UUID.randomUUID(), "America-bean",
		8000L, 100, null, LocalDateTime.now(), LocalDateTime.now());
	private Order order = new Order(UUID.randomUUID(),
		Email.of("abc@naver.com"),
		"abc - STreet",
		"22001",
		List.of(
			new OrderItem(kenya.getProductId(), 2)
		),
		OrderStatus.ACCEPTED,
		LocalDateTime.now(),
		LocalDateTime.now()
	);

	@BeforeEach
	public void setup() {
		productRepository.insert(kenya);
		productRepository.insert(america);
		orderRepository.insert(order);
	}

	@Test
	@DisplayName("주문을 취소상태로 변경한다")
	public void given_order_when_changeStatusAsCanceled_thenChanged() {
		orderRepository.update(order.getOrderId(), OrderStatus.CANCELED);

		Order foundOrder = orderRepository.findById(order.getOrderId()).get();
		Assertions.assertThat(foundOrder.getOrderStatus())
			.isEqualTo(OrderStatus.CANCELED);
	}

	@Test
	@DisplayName("주문을 취소상태로 변경 후, 이전상태와 비교한다")
	public void given_order_when_compareChangedOrderAndBefore_thenSuccess() {
		OrderStatus beforeStatus = order.getOrderStatus();

		orderRepository.update(order.getOrderId(), OrderStatus.CANCELED);

		Order foundOrder = orderRepository.findById(order.getOrderId()).get();
		Assertions.assertThat(foundOrder.getOrderStatus())
			.isNotEqualTo(beforeStatus);
	}

	@Test
	@DisplayName("주문 취소를 위해, 주문에 포함된 아이템들을 orderItems 에서 삭제한다")
	public void given_orderTobeCanceled_when_deleteOrderedItemsAndCountUpdatedRows_thenSuccess() {
		long orderedItemCount = order.getOrderItems()
			.stream()
			.count();
		int deletedCount = orderRepository.deleteItems(order.getOrderId());

		Assertions.assertThat(deletedCount)
			.isEqualTo(orderedItemCount);
	}

	@Test
	@DisplayName("존재하지 않는 주문에 대한 update 요청시 Exception 이 발생하지 않는다 ")
	public void given_newId_when_tryUpdateUsingThisId_thenThrowException() {
		UUID newId = UUID.randomUUID();

		assertDoesNotThrow(() -> orderRepository.update(newId, OrderStatus.CANCELED));
	}

	@Test
	@DisplayName("존재 하지 않는 주문에 대한 update 요청시 update 된 row 는 0개다")
	public void given_newId_when_tryUpdateUsingThisId_thenUpdateCountIsZero() {
		UUID newId = UUID.randomUUID();

		int updatedCount = orderRepository.update(newId, OrderStatus.CANCELED);

		Assertions.assertThat(updatedCount).isEqualTo(0);
	}

}