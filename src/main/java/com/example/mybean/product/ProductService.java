package com.example.mybean.product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mybean.commons.exception.NotFoundException;
import com.example.mybean.product.exception.StockOutException;
import com.example.mybean.product.repository.ProductRepository;

@Service
public class ProductService {
	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@Transactional
	public void sell(UUID productId, int quantity) throws StockOutException {
		if (quantity <= 0) {
			throw new IllegalArgumentException("감소시키려는 양은 0 보다 커야 합니다");
		}
		Product product = this.getById(productId);

		product.decreaseStock(quantity);
		productRepository.update(product);
	}

	@Transactional
	public Product getById(UUID productId) {
		return productRepository.findById(productId)
			.orElseThrow(() ->
				new NotFoundException(productId.toString()));
	}

	@Transactional
	public Product createProduct(String productName, long price, int stock, String description) {
		Product product = new Product(UUID.randomUUID(), productName, price, stock, description, LocalDateTime.now(), LocalDateTime.now());

		return productRepository.insert(product);
	}
}
