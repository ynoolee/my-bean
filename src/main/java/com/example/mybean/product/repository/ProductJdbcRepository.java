package com.example.mybean.product.repository;

import static com.example.mybean.order.commons.JdbcUtils.*;

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

import com.example.mybean.product.Product;

@Repository
public class ProductJdbcRepository implements ProductRepository {
	private final NamedParameterJdbcTemplate jdbcTemplate;

	public ProductJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Optional<Product> findById(UUID productId) {
		try {
			return Optional.of(
				jdbcTemplate.queryForObject(
					"SELECT * FROM products WHERE product_id = UUID_TO_BIN(:productId) FOR UPDATE",
					Collections.singletonMap("productId", productId.toString().getBytes()), productRowMapper)
			);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public List<Product> findAll() {
		return jdbcTemplate.query("SELECT * FROM products", productRowMapper);
	}

	@Override
	public Product insert(Product product) {
		int update = jdbcTemplate.update(
			"INSERT INTO products(product_id, product_name, price, stock, description, created_at, updated_at)" +
				" VALUES(UUID_TO_BIN(:productId), :productName, :price, :stock, :description, :createdAt, :updatedAt )",
			toParamMap(product));

		if (update != 1) {
			throw new RuntimeException("Product insert 실패");
		}

		return product;
	}

	@Override
	public Product update(Product product) {
		int update = jdbcTemplate.update(
			"UPDATE products SET product_name = :productName, price = :price, stock = :stock, description = :description, created_at = :createdAt,  updated_at = :updatedAt"
				+ " WHERE product_id = UUID_TO_BIN(:productId)",
			toParamMap(product)
		);

		if (update != 1) {
			throw new RuntimeException("업데이트가 수행되지 않았습니다");
		}

		return product;
	}

	@Override
	public Optional<Product> findByName(String productName) {
		try {
			return Optional.of(
				jdbcTemplate.queryForObject("SELECT * FROM products WHERE product_name = :productName",
					Collections.singletonMap("productName", productName.toString()), productRowMapper)
			);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public void deleteAll() {
		jdbcTemplate.update("DELETE FROm products", Collections.emptyMap());
	}

	private static final RowMapper<Product> productRowMapper = (rs, i) -> {
		UUID productId = toUUID(rs.getBytes("product_id"));
		String productName = rs.getString("product_name");
		long price = rs.getLong("price");
		int stock = rs.getInt("stock");
		String description = rs.getString("description");
		LocalDateTime createdAt = toLocalDateTime(rs.getTimestamp("created_at"));
		LocalDateTime updatedAt = toLocalDateTime(rs.getTimestamp("updated_at"));

		return new Product(productId,
			productName,
			price,
			stock,
			description,
			createdAt,
			updatedAt);
	};

	private Map<String, Object> toParamMap(Product product) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("productId", product.getProductId().toString().getBytes());
		paramMap.put("productName", product.getProductName());
		paramMap.put("price", product.getPrice());
		paramMap.put("stock", product.getStock());
		paramMap.put("description", product.getDescription());
		paramMap.put("createdAt", product.getCreatedAt());
		paramMap.put("updatedAt", product.getUpdatedAt());

		return paramMap;
	}
}
