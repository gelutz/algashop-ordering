package com.lutz.algashop.ordering.domain.order.service;

import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.commons.Quantity;
import com.lutz.algashop.ordering.domain.customer.Customer;
import com.lutz.algashop.ordering.domain.customer.CustomerId;
import com.lutz.algashop.ordering.domain.customer.LoyaltyPoints;
import com.lutz.algashop.ordering.domain.customer.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.domain.order.Billing;
import com.lutz.algashop.ordering.domain.order.BuyNowService;
import com.lutz.algashop.ordering.domain.order.Orders;
import com.lutz.algashop.ordering.domain.order.builder.OrderTestBuilder;
import com.lutz.algashop.ordering.domain.order.entity.Order;
import com.lutz.algashop.ordering.domain.order.entity.PaymentMethod;
import com.lutz.algashop.ordering.domain.order.shipping.Shipping;
import com.lutz.algashop.ordering.domain.order.specification.CustomerHasFreeShippingSpecification;
import com.lutz.algashop.ordering.domain.product.Product;
import com.lutz.algashop.ordering.domain.product.ProductOutOfStockException;
import com.lutz.algashop.ordering.domain.product.builder.ProductTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class BuyNowServiceTest {
	private BuyNowService sut;

	@Mock
	private Orders orders;

	@BeforeEach
	void setup() {
		CustomerHasFreeShippingSpecification customerHasFreeShippingSpecification = new CustomerHasFreeShippingSpecification(
				orders,
				new LoyaltyPoints(100),
				2,
				new LoyaltyPoints(2000)
		);

		sut = new BuyNowService(customerHasFreeShippingSpecification);
	}

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
			Customer customer = CustomerTestBuilder.aCustomer().withId(customerId).build();

			Order order = sut.buyNow(product, customer, billing, shipping, quantity, paymentMethod);

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
			Customer customer = CustomerTestBuilder.aCustomer().withId(customerId).build();
			Quantity quantity = new Quantity(1);

			assertThatThrownBy(() -> sut.buyNow(product, customer, billing, shipping, quantity, paymentMethod))
					.isInstanceOf(ProductOutOfStockException.class);
		}

		@Test
		@DisplayName("dado quantidade zero deve lançar IllegalArgumentException")
		void givenZeroQuantityShouldThrowIllegalArgumentException() {
			Product product = ProductTestBuilder.aProduct().build();
			Customer customer = CustomerTestBuilder.aCustomer().withId(customerId).build();
			Quantity quantity = new Quantity(0);

			assertThatThrownBy(() -> sut.buyNow(product, customer, billing, shipping, quantity, paymentMethod))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		@DisplayName("dado customer com compras e pontos suficientes, deve receber free shipping quando fizer uma order")
		void givenCustomerWithEnoughPointsShouldHaveFreeShipping() {
			Mockito.when(orders.salesQuantityByCustomerInYear(
					Mockito.any(CustomerId.class),
					Mockito.any(Year.class)
			)).thenReturn(2L);
			Product product = ProductTestBuilder.aProduct().build();
			Quantity quantity = new Quantity(2);
			Customer customer = CustomerTestBuilder.aCustomer()
			                                       .withId(customerId)
			                                       .withLoyaltyPoints(new LoyaltyPoints(100))
			                                       .build();

			Order order = sut.buyNow(product, customer, billing, shipping, quantity, paymentMethod);
			Shipping freeShippingExpectedResult = shipping.toBuilder().cost(Money.ZERO).build();

			assertThat(order).isNotNull();
			assertThat(order.isPlaced()).isTrue();
			assertThat(order.customerId()).isEqualTo(customerId);
			assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
			assertThat(order.billing()).isEqualTo(billing);
			assertThat(order.shipping()).isEqualTo(freeShippingExpectedResult);
			assertThat(order.isPlaced()).isTrue();

			assertThat(order.items()).hasSize(1);
			assertThat(order.items().iterator().next().productId()).isEqualTo(product.id());
			assertThat(order.items().iterator().next().quantity()).isEqualTo(quantity);
			assertThat(order.items().iterator().next().price()).isEqualTo(product.price());

			Money expectedTotalAmount = product.price().multiply(quantity);
			assertThat(order.totalAmount()).isEqualTo(expectedTotalAmount);
			assertThat(order.itemsAmount()).isEqualTo(quantity);
		}
	}
}
