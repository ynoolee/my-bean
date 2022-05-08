package com.example.mybean.product.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.mybean.product.model.Product;

public interface ProductRepository {
	List<Product> findAll();

	Product insert(Product product);

	Product update(Product product);

	Optional<Product> findById(UUID productId);

	Optional<Product> findByName(String productName);

	void deleteAll();

	void deleteById(UUID productId);
}
