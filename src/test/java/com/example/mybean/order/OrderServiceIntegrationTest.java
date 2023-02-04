package com.example.mybean.order;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.mybean.order.model.OrderItem;
import com.example.mybean.product.ProductService;
import com.example.mybean.product.exception.StockOutException;
import com.example.mybean.product.model.Product;

@Disabled
@SpringBootTest
@ActiveProfiles("test")
class OrderServiceIntegrationTest {
	@Autowired
	OrderService orderService;

	@Autowired
	ProductService productService;

	private Product richProduct;
	private Product productToBeSoldOut;
	private final int richProductInitStock = 10;
	private final int toBeSoldProductInitStock = 3;
	private final int soldQuantity = 4;

	@BeforeEach
	public void setUp(){
		richProduct = productService.createProduct("충분 상품", 10000L, richProductInitStock, null);
		productToBeSoldOut = productService.createProduct("품절될 상품", 10000L, toBeSoldProductInitStock, null);
	}

	@Test
	@DisplayName("주문 성공 시 재고가 줄어든다")
	public void given_order_when_tryToOrder_then_productStockIsReduced() {

		orderService.createOrder("abc@naver.com",
			"Triple-street",
			"22001",
			List.of(new OrderItem(richProduct.getProductId(),soldQuantity)));

		Product foundProduct = productService.getById(richProduct.getProductId());

		assertThat(foundProduct.getStock())
			.isEqualTo(richProductInitStock-soldQuantity);
	}

	@Test
	@DisplayName("주문하는 아이템 중 하나의 재고가 부족하면 StockOutException 을 throw 한다")
	public void given_orderWhichIncludeOutOfStockProduct_when_tryToOrder_then_throwException() {
		assertThatThrownBy(() ->
			orderService.createOrder("abc@naver.com",
				"Triple Street",
				"22001",
				List.of(new OrderItem(productToBeSoldOut.getProductId(), soldQuantity)))
		).isInstanceOf(StockOutException.class);
	}

	@Test
	@DisplayName("주문하는 아이템 중 하나의 재고가 부족하면 주문과정을 롤백하여 프로덕트 개수가 주문 전과 같다")
	public void given_orderWhichIncludeOutOfStockProduct_when_tryToOrder_then_theNuberOfProductIsSameAsBefore() {
		assertThatThrownBy(() ->
			orderService.createOrder("abc@naver.com", "Triple Street", "22001",
				List.of(new OrderItem(richProduct.getProductId(), soldQuantity),
					new OrderItem(productToBeSoldOut.getProductId(), soldQuantity))))
			.isInstanceOf(StockOutException.class);

		Product productToBeSoldOutFound = productService.getById(productToBeSoldOut.getProductId());
		Product richProductFound = productService.getById(richProduct.getProductId());

		assertThat(productToBeSoldOutFound.getStock()).isEqualTo(productToBeSoldOut.getStock());
		assertThat(richProductFound.getStock()).isEqualTo(richProduct.getStock());
	}

}