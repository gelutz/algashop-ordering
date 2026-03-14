package com.lutz.algashop.ordering.domain.service;

import com.lutz.algashop.ordering.domain.entity.builder.OrderTestBuilder;
import com.lutz.algashop.ordering.domain.entity.builder.ProductTestBuilder;
import com.lutz.algashop.ordering.domain.entity.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.entity.order.Order;
import com.lutz.algashop.ordering.domain.entity.order.PaymentMethod;
import com.lutz.algashop.ordering.domain.entity.order.vo.*;
import com.lutz.algashop.ordering.domain.exception.order.ProductOutOfStockException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BuyNowServiceTest {

	private final BuyNowService sut = new BuyNowService();

	private final CustomerId customerId = new CustomerId();
	private final Billing billing = OrderTestBuilder.aBilling().build();
	private final Shipping shipping = OrderTestBuilder.aShipping().build();
	private final PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;

	@Nested
	@DisplayName("buyNow")
	class BuyNowTests {

		@Test
		@DisplayName("dado produto válido deve criar pedido com status PLACED")
		void givenValidProductShouldCreatePlacedOrder() {
			Product product = ProductTestBuilder.aProduct().build();
			Quantity quantity = new Quantity(2);

			Order order = sut.buyNow(product, customerId, billing, shipping, quantity, paymentMethod);

			assertThat(order).isNotNull();
			assertThat(order.isPlaced()).isTrue();
			assertThat(order.customerId()).isEqualTo(customerId);
			assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
			assertThat(order.billing()).isEqualTo(billing);
			assertThat(order.shipping()).isEqualTo(shipping);
			assertThat(order.items()).hasSize(1);
			assertThat(order.itemsAmount()).isEqualTo(new Quantity(2));
			assertThat(order.totalAmount()).isEqualTo(new Money("60.00"));
		}

		@Test
		@DisplayName("dado produto fora de estoque deve lançar ProductOutOfStockException")
		void givenOutOfStockProductShouldThrowProductOutOfStockException() {
			Product product = ProductTestBuilder.aProductUnavailable().build();
			Quantity quantity = new Quantity(1);

			assertThatThrownBy(() -> sut.buyNow(product, customerId, billing, shipping, quantity, paymentMethod))
					.isInstanceOf(ProductOutOfStockException.class);
		}

		@Test
		@DisplayName("dado quantidade zero deve lançar IllegalArgumentException")
		void givenZeroQuantityShouldThrowIllegalArgumentException() {
			Product product = ProductTestBuilder.aProduct().build();
			Quantity quantity = new Quantity(0);

			assertThatThrownBy(() -> sut.buyNow(product, customerId, billing, shipping, quantity, paymentMethod))
					.isInstanceOf(IllegalArgumentException.class);
		}
	}
}
