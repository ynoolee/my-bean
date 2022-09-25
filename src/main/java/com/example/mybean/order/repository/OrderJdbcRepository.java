package com.example.mybean.order.repository;

import static com.example.mybean.commons.JdbcUtils.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.mybean.order.model.Email;
import com.example.mybean.order.model.Order;
import com.example.mybean.order.model.OrderItem;
import com.example.mybean.order.model.OrderStatus;

@Repository
public class OrderJdbcRepository implements OrderRepository {
	private final NamedParameterJdbcTemplate jdbcTemplate;

	public OrderJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	@Transactional
	public Order insert(Order order) {
		jdbcTemplate.update(
			"INSERT INTO orders(order_id, email, address, postcode, order_status, created_at,updated_at) " +
				"VALUES(UUID_TO_BIN(:orderId), :email, :address, :postcode, :orderStatus, :createdAt,:updatedAt)",
			toOrderedParamMap(order));

		order.getOrderItems()
			.forEach(item -> jdbcTemplate.update(
				"INSERT INTO order_items(order_id, product_id, quantity, created_at, updated_at) "
					+
					"VALUES (UUID_TO_BIN(:orderId), UUID_TO_BIN(:productId), :quantity, :createdAt, :updatedAt)",
				toOrderItemParamMap(order.getOrderId(), order.getCreatedAt(), order.getUpdatedAt(), item)));

		return order;
	}

	@Override
	public int update(UUID orderId, OrderStatus orderStatus) {
		return jdbcTemplate.update(
			"UPDATE orders SET order_status = :orderStatus"
				+ " WHERE order_id = UUID_TO_BIN(:orderId)",
			Map.of("orderId", orderId.toString().getBytes(),
				"orderStatus", orderStatus.toString())
		);
	}

	@Override
	public Optional<Order> findById(UUID orderId) {
		try {
			return Optional.of(
				jdbcTemplate.queryForObject("SELECT * FROM orders WHERE order_id = UUID_TO_BIN(:orderId)",
					Collections.singletonMap("orderId", orderId.toString().getBytes()), orderRowMapper)
			);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public int deleteItems(UUID orderId) {
		return jdbcTemplate.update("DELETE FROM order_items WHERE order_id = UUID_TO_BIN(:orderId)",
			Map.of("orderId", orderId.toString().getBytes()));
	}

	@Override
	public void deleteAll() {
		jdbcTemplate.update("DELETE FROM order_items", Collections.emptyMap());

		jdbcTemplate.update("DELETE FROM orders", Collections.emptyMap());
	}

	private Map<String, Object> toOrderedParamMap(Order order) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("orderId", order.getOrderId().toString().getBytes());
		paramMap.put("email", order.getEmail().getAddress());
		paramMap.put("address", order.getAddress());
		paramMap.put("postcode", order.getPostcode());
		paramMap.put("orderStatus", order.getOrderStatus().toString());
		paramMap.put("createdAt", order.getCreatedAt());
		paramMap.put("updatedAt", order.getUpdatedAt());

		return paramMap;
	}

	private Map<String, Object> toOrderItemParamMap(UUID orderId, LocalDateTime createdAt, LocalDateTime updatedAt,
		OrderItem item) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("orderId", orderId.toString().getBytes());
		paramMap.put("productId", item.productId().toString().getBytes());
		paramMap.put("quantity", item.quantity());
		paramMap.put("createdAt", createdAt);
		paramMap.put("updatedAt", updatedAt);

		return paramMap;
	}

	private static final RowMapper<Order> orderRowMapper = (rs, i) -> {
		UUID orderId = toUUID(rs.getBytes("order_id"));
		Email email = Email.of(rs.getString("email"));
		String address = rs.getString("address");
		String postcode = rs.getString("postcode");
		OrderStatus orderStatus = OrderStatus.of(rs.getString("order_status"));
		LocalDateTime createdAt = toLocalDateTime(rs.getTimestamp("created_at"));
		LocalDateTime updatedAt = toLocalDateTime(rs.getTimestamp("updated_at"));

		return new Order(orderId,
			email,
			address,
			postcode,
			List.of(),
			orderStatus,
			createdAt,
			updatedAt);
	};
}
