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

import com.example.mybean.commons.ApiResult;
import com.example.mybean.product.dto.CreateProductRequest;
import com.example.mybean.product.dto.UpdateProductInfoRequest;
import com.example.mybean.product.model.Product;

@RestController
public class ProductRestController {
	private final ProductService productService;

	public ProductRestController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("/api/products")
	public ResponseEntity<ApiResult<List<Product>>> productList() {
		List<Product> products = productService.getAllProducts();

		return ResponseEntity.ok(
			new ApiResult(products.stream().count(),
				products));
	}

	@PostMapping("/api/products")
	public ResponseEntity<ApiResult<Product>> newProduct(@RequestBody CreateProductRequest createProductRequest) {
		return ResponseEntity.ok(
			new ApiResult(
				productService.createProduct(
					createProductRequest.productName(), createProductRequest.price(), createProductRequest.stock(),
					createProductRequest.description())));
	}

	@PutMapping("/api/products/{productId}")
	public ResponseEntity<ApiResult<Product>> updateProductInfo(@PathVariable UUID productId,
		@RequestBody UpdateProductInfoRequest updatedInfo) {
		return ResponseEntity.ok(
			new ApiResult(
				productService.changeInfo(productId,
					updatedInfo.productName(),
					updatedInfo.price(),
					updatedInfo.description())));
	}

	@GetMapping("/api/products/{productId}")
	public ResponseEntity<ApiResult<Product>> requestProductInfo(@PathVariable UUID productId) {
		return ResponseEntity.ok(
			new ApiResult(productService.getById(productId))
		);
	}
}
