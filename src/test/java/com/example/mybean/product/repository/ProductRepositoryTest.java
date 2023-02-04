package com.example.mybean.product.repository;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import com.example.mybean.order.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;

import com.example.mybean.product.model.Product;

@Disabled
@SpringBootTest
@ActiveProfiles("test")
public class ProductRepositoryTest {
	Product newProduct = new Product(UUID.randomUUID(), "Kenya-bean", 1000L, 4, null, LocalDateTime.now(),
		LocalDateTime.now());

	@Autowired
	ProductRepository repository;

	@Autowired
	OrderRepository orderRepository;

	@BeforeEach
	public void setup() {
		repository.insert(newProduct);
	}

	@AfterEach
	void tearDown() {
		orderRepository.deleteAll();
		repository.deleteAll();
	}

	@Test
	@DisplayName("상품을 추가 할 수 있다")
	public void testInsert() {
		List<Product> products = repository.findAll();

		assertThat(products.isEmpty(), is(false));
	}

	@Test
	@DisplayName("상품을 이름으로 조회할 수 있다")
	public void testFindByName() {
		Optional<Product> product = repository.findByName(newProduct.getProductName());

		assertThat(product.isEmpty(), is(false));
	}

	@Test
	@DisplayName("상품을 Id 로 조회할 수 있다")
	public void testFindById() {
		Optional<Product> product = repository.findById(newProduct.getProductId());

		assertThat(product.isEmpty(), is(false));
	}

	@Test
	@DisplayName("상품을 전체 삭제한다")
	void testDeleteAll() {
		repository.deleteAll();

		List<Product> products = repository.findAll();

		assertThat(products.isEmpty(), is(true));
	}

	@Test
	@DisplayName("상품 수정 가능")
	void testUpdate() {
		newProduct.changeProductName("updated-product");
		repository.update(newProduct);

		Optional<Product> product = repository.findById(newProduct.getProductId());

		assertThat(product.isEmpty(), is(false));
		assertThat(product.get(), samePropertyValuesAs(newProduct));
	}

	@Test
	@DisplayName("상품 재고를 감소시키는 업데이트가 가능하다")
	void testDecreaseStock() {
		int decreasedAmount = 1;
		int beforeStock = newProduct.getStock();

		newProduct.decreaseStock(decreasedAmount);
		repository.update(newProduct);

		Product foundProduct = repository.findById(newProduct.getProductId()).get();

		assertThat(foundProduct.getStock(), is(beforeStock - decreasedAmount));
	}

	@Test
	@DisplayName("중복 ID 를 갖는 Product 를 insert 시 DuplicateKeyException 예외가 발생한다")
	void given_dupId_when_createDuplicatedProductAndTryInset_then_throwException() {
		UUID duplicatedId = newProduct.getProductId();

		Product dupProduct = new Product(duplicatedId, "a", 100L, 10, null, LocalDateTime.now(), LocalDateTime.now());

		Assertions.assertThatThrownBy(() ->
				repository.insert(dupProduct))
			.isInstanceOf(DuplicateKeyException.class);
	}

}
