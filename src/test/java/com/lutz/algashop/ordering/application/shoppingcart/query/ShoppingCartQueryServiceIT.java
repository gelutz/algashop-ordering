package com.lutz.algashop.ordering.application.shoppingcart.query;

import com.lutz.algashop.ordering.application.shoppingcart.management.ShoppingCartItemInput;
import com.lutz.algashop.ordering.application.shoppingcart.management.ShoppingCartManagementApplicationService;
import com.lutz.algashop.ordering.domain.customer.Customers;
import com.lutz.algashop.ordering.domain.customer.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.domain.product.Product;
import com.lutz.algashop.ordering.domain.product.ProductCatalogService;
import com.lutz.algashop.ordering.domain.product.builder.ProductTestBuilder;
import com.lutz.algashop.ordering.domain.shoppingCart.exception.ShoppingCartNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
class ShoppingCartQueryServiceIT {

	@Autowired
	private ShoppingCartQueryService sut;

	@Autowired
	private ShoppingCartManagementApplicationService managementService;

	@Autowired
	private Customers customers;

	@MockitoBean
	private ProductCatalogService productCatalogService;

	private UUID cartId;
	private Product product;

	@BeforeEach
	void setup() {
		if (!customers.exists(CustomerTestBuilder.DEFAULT_CUSTOMER_ID)) {
			customers.add(CustomerTestBuilder.aCustomer().build());
		}

		product = ProductTestBuilder.aProduct().build();
		Mockito.when(productCatalogService.ofId(product.id()))
				.thenReturn(java.util.Optional.of(product));

		cartId = managementService.createNew(CustomerTestBuilder.DEFAULT_CUSTOMER_ID.value());

		managementService.addItem(ShoppingCartItemInput.builder()
				.shoppingCartId(cartId)
				.productId(product.id().value())
				.quantity(2)
				.build());
	}

	@Nested
	class FindById {

		@Test
		void shouldReturnOutputWhenCartExists() {
			ShoppingCartOutput output = sut.findById(cartId);

			assertThat(output).isNotNull();
			assertThat(output.getId()).isEqualTo(cartId);
			assertThat(output.getCustomerId()).isEqualTo(CustomerTestBuilder.DEFAULT_CUSTOMER_ID.value());
			assertThat(output.getTotalItems()).isEqualTo(2);
			assertThat(output.getTotalAmount()).isEqualByComparingTo(new BigDecimal("50.00"));
			assertThat(output.getItems()).hasSize(1);

			ShoppingCartItemOutput item = output.getItems().get(0);
			assertThat(item.getProductId()).isEqualTo(product.id().value());
			assertThat(item.getName()).isEqualTo("Test Product");
			assertThat(item.getPrice()).isEqualByComparingTo(new BigDecimal("25.00"));
			assertThat(item.getQuantity()).isEqualTo(2);
			assertThat(item.getTotalAmount()).isEqualByComparingTo(new BigDecimal("50.00"));
			assertThat(item.getAvailable()).isTrue();
		}

		@Test
		void shouldThrowWhenCartNotFound() {
			assertThatExceptionOfType(ShoppingCartNotFoundException.class)
					.isThrownBy(() -> sut.findById(UUID.randomUUID()));
		}
	}

	@Nested
	class FindByCustomerId {

		@Test
		void shouldReturnOutputWhenCustomerHasCart() {
			ShoppingCartOutput output = sut.findByCustomerId(CustomerTestBuilder.DEFAULT_CUSTOMER_ID.value());

			assertThat(output).isNotNull();
			assertThat(output.getId()).isEqualTo(cartId);
			assertThat(output.getCustomerId()).isEqualTo(CustomerTestBuilder.DEFAULT_CUSTOMER_ID.value());
			assertThat(output.getTotalItems()).isEqualTo(2);
			assertThat(output.getTotalAmount()).isEqualByComparingTo(new BigDecimal("50.00"));
			assertThat(output.getItems()).hasSize(1);

			ShoppingCartItemOutput item = output.getItems().get(0);
			assertThat(item.getName()).isEqualTo("Test Product");
			assertThat(item.getQuantity()).isEqualTo(2);
		}

		@Test
		void shouldThrowWhenCustomerHasNoCart() {
			assertThatExceptionOfType(ShoppingCartNotFoundException.class)
					.isThrownBy(() -> sut.findByCustomerId(UUID.randomUUID()));
		}
	}
}
