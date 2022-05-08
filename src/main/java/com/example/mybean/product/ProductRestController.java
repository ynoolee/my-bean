package com.example.mybean.product;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.mybean.product.dto.CreateProductRequest;
import com.example.mybean.product.dto.UpdateProductInfoRequest;

@RestController
public class ProductRestController {
	private final ProductService productService;

	public ProductRestController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("/api/products")
	public ResponseEntity<List<Product>> productList() {
		return ResponseEntity.ok(
			productService.getAllProducts()
		);
	}

	@PostMapping("/api/products")
	public ResponseEntity<Product> newProduct(@RequestBody CreateProductRequest createProductRequest) {
		return ResponseEntity.ok(
			productService.createProduct(
				createProductRequest.productName(), createProductRequest.price(), createProductRequest.stock(),
				createProductRequest.description()
			)
		);
	}

	@PutMapping("/api/products/{productId}")
	public ResponseEntity<Product> updateProductInfo(@PathVariable UUID productId,
		@RequestBody UpdateProductInfoRequest updatedInfo) {
		return ResponseEntity.ok(
			productService.changeInfo(productId,
				updatedInfo.productName(),
				updatedInfo.price(),
				updatedInfo.description())
		);
	}

}
