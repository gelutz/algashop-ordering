package com.lutz.algashop.ordering.domain.entity.order;

import com.lutz.algashop.ordering.domain.entity.builder.OrderTestBuilder;
import com.lutz.algashop.ordering.domain.entity.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.entity.order.vo.Money;
import com.lutz.algashop.ordering.domain.entity.order.vo.OrderItemId;
import com.lutz.algashop.ordering.domain.entity.order.vo.Quantity;
import com.lutz.algashop.ordering.domain.entity.order.vo.Shipping;
import com.lutz.algashop.ordering.domain.exception.messages.ErrorMessages;
import com.lutz.algashop.ordering.domain.exception.InvalidShippingDeliveryDateException;
import com.lutz.algashop.ordering.domain.exception.order.OrderCannotBeEditedException;
import com.lutz.algashop.ordering.domain.exception.order.OrderDoesNotContainOrderItemException;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class OrderChangeMethodsTest {
	@Nested
	@DisplayName("Order#verifyIfChangeable tests")
	class VerifyIfChangeableTests {
		private Order sut;

		@BeforeEach
		void setup() {
			sut = Order.draft(new CustomerId());
			sut.changeShipping(OrderTestBuilder.aShipping().build());
			sut.changeBilling(OrderTestBuilder.aBilling().build());
			sut.changePaymentMethod(PaymentMethod.GATEWAY_BALANCE);
			sut.addItem(OrderTestBuilder.aProduct().build(), new Quantity(1));
		}
		@Test
		void givenDraftOrderShouldBeChangeable() {
			OrderItemId orderItemId = sut.items().iterator().next().id();

			Assertions.assertTrue(sut.isDraft());
			Assertions.assertDoesNotThrow(() -> sut.changePaymentMethod(PaymentMethod.CREDIT_CARD));
			Assertions.assertDoesNotThrow(() -> sut.changeShipping(OrderTestBuilder.aShipping().build()));
			Assertions.assertDoesNotThrow(() -> sut.changeBilling(OrderTestBuilder.aBilling().build()));
			Assertions.assertDoesNotThrow(() -> sut.changeItemQuantity(orderItemId, new Quantity(10)));
		}

		@Test
		void givenNotDraftOrderChangingShouldThrowOrderCannotBeEditedException() {
			sut.place();

			OrderItemId orderItemId = sut.items().iterator().next().id();

			Assertions.assertTrue(sut.isPlaced());
			Assertions.assertThrows(OrderCannotBeEditedException.class, () -> sut.changePaymentMethod(PaymentMethod.GATEWAY_BALANCE));
			Assertions.assertThrows(OrderCannotBeEditedException.class, () -> sut.changeShipping(OrderTestBuilder.aShipping().build()));
			Assertions.assertThrows(OrderCannotBeEditedException.class, () -> sut.changeBilling(OrderTestBuilder.aBilling().build()));
			Assertions.assertThrows(OrderCannotBeEditedException.class, () -> sut.changeItemQuantity(orderItemId, new Quantity(10)));
		}
	}

	@Nested
	@DisplayName("Order#changeShippingInfo tests")
	class ChangeShippingTests {
		private Order sut;
		private Shipping shipping;

		@BeforeEach
		void setUp() {
			shipping = OrderTestBuilder.aShipping().build();
			sut = OrderTestBuilder.anExistingOrder()
			                      .withStatus(OrderStatus.DRAFT)
			                      .withShipping(shipping)
			                      .build();
		}

		@Test
		@DisplayName("Should update shipping info with valid fields")
		void shouldUpdateShippingInfoWithValidFutureDeliveryDate() {
			sut.changeShipping(shipping);


			assertNotNull(sut.shipping().address());
			assertNotNull(sut.shipping().cost());
			assertNotNull(sut.shipping().expectedDeliveryDate());
			assertNotNull(sut.shipping().recipient());
			assertEquals(shipping, sut.shipping());
			assertEquals(shipping.cost(), sut.shipping().cost());
		}

		@Test
		@DisplayName("Should throw InvalidShippingDeliveryDateException when date is in the past")
		void shouldThrowInvalidShippingDeliveryDateExceptionWhenDateIsInThePast() {
			LocalDate pastDate = LocalDate.now().minusDays(1);

			shipping = OrderTestBuilder.aShipping()
			                           .expectedDeliveryDate(pastDate)
			                           .build();

			InvalidShippingDeliveryDateException exception =
					assertThrows(InvalidShippingDeliveryDateException.class, () -> {
						sut.changeShipping(shipping);
					});

			assertEquals(
					ErrorMessages.Orders.orderExpectedDeliveryDateIsInvalid(sut.id(), pastDate),
					exception.getMessage()
			            );
		}
	}

	@Nested
	@DisplayName("Order#changeItemQuantity tests")
	class ChangeItemQuantityTests {
		private Order sut;

		@BeforeEach
		void setUp() {
			Money price = new Money(new BigDecimal("25.00"));
			Quantity quantity = new Quantity(2);
			Money totalAmount = new Money(price.value().multiply(BigDecimal.valueOf(quantity.value())));

			sut = OrderTestBuilder.anExistingOrder()
			                      .withStatus(OrderStatus.DRAFT)
			                      .withProducts(Set.of(OrderTestBuilder.aProduct().build()))
			                      .withTotalAmount(totalAmount)
			                      .withItemsAmount(quantity)
			                      .build();

		}

		@Test
		@DisplayName("Should throw OrderDoesNotContainOrderItemException when item not found")
		void shouldThrowExceptionWhenItemNotFound() {
			OrderItemId nonExistentItemId = new OrderItemId();

			OrderDoesNotContainOrderItemException exception =
					assertThrows(OrderDoesNotContainOrderItemException.class, () -> {
						sut.changeItemQuantity(nonExistentItemId, new Quantity(5));
					});

			assertEquals(
					ErrorMessages.Orders.orderDoesNotContainOrderItem(sut.id(), nonExistentItemId),
					exception.getMessage()
			            );
		}

		@Test
		@DisplayName("Should change quantity of existing item")
		void shouldChangeQuantityOfExistingItem() {
			Quantity newQuantity = new Quantity(5);
			OrderItem existingItem = sut.items().iterator().next();

			sut.changeItemQuantity(existingItem.id(), newQuantity);

			OrderItem updatedItem = sut.items().stream()
			                           .filter(i -> i.id().equals(existingItem.id()))
			                           .findFirst()
			                           .orElseThrow();
			assertEquals(newQuantity, updatedItem.quantity());
		}

		@Test
		@DisplayName("Should recalculate totals after changing item quantity")
		void shouldRecalculateTotalsAfterChangingItemQuantity() {
			Money initialTotal = sut.totalAmount();
			Quantity initialItemsAmount = sut.itemsAmount();
			OrderItem existingItem = sut.items().iterator().next();

			Quantity newQuantity = new Quantity(10);
			sut.changeItemQuantity(existingItem.id(), newQuantity);

			assertNotEquals(initialTotal, sut.totalAmount());
			assertNotEquals(initialItemsAmount, sut.itemsAmount());

			BigDecimal expectedItemTotal = existingItem.price().value()
			                                           .multiply(BigDecimal.valueOf(newQuantity.value()));
			assertTrue(sut.totalAmount().value().compareTo(expectedItemTotal) >= 0);
			assertEquals(newQuantity, sut.itemsAmount());
		}
	}
}
