package com.lutz.algashop.ordering.domain.entity.order;

import com.lutz.algashop.ordering.domain.entity.builder.OrderTestBuilder;
import com.lutz.algashop.ordering.domain.exception.ErrorMessages;
import com.lutz.algashop.ordering.domain.exception.order.OrderStatusCannotBeChangedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderStatusTest {


	@Test
	void givenValidNewStatusCanChangeShouldReturnTrue() {
		Assertions.assertTrue(OrderStatus.DRAFT.canChangeTo(OrderStatus.PLACED));
		Assertions.assertTrue(OrderStatus.PLACED.canChangeTo(OrderStatus.PAID));
		Assertions.assertTrue(OrderStatus.PAID.canChangeTo(OrderStatus.READY));


		Assertions.assertTrue(OrderStatus.DRAFT.canChangeTo(OrderStatus.CANCELED));
		Assertions.assertTrue(OrderStatus.PLACED.canChangeTo(OrderStatus.CANCELED));
		Assertions.assertTrue(OrderStatus.PAID.canChangeTo(OrderStatus.CANCELED));
		Assertions.assertTrue(OrderStatus.READY.canChangeTo(OrderStatus.CANCELED));
	}

	@Test
	void givenInvalidNewStatusCannotChangeShouldReturnTrue() {
		Assertions.assertTrue(OrderStatus.PLACED.cannotChangeTo(OrderStatus.DRAFT));
		Assertions.assertTrue(OrderStatus.DRAFT.cannotChangeTo(OrderStatus.PAID));
		Assertions.assertTrue(OrderStatus.PAID.cannotChangeTo(OrderStatus.PAID));
		Assertions.assertTrue(OrderStatus.READY.cannotChangeTo(OrderStatus.PAID));
	}

	@Nested
	@DisplayName("Order#place tests")
	class OrderPlaceTests {

		@Test
		void givenDraftOrderPlaceShouldSetOrderAsPlaced() {
			Order sut = OrderTestBuilder.aFilledDraftOrder().build();
			sut.place();

			assertTrue(sut.isPlaced());
		}

		@Test
		void givenPlacedOrderPlaceShouldThrowOrderStatusCannotBeChangedException() {
			Order sut = OrderTestBuilder.aFilledDraftOrder().build();
			sut.place();

			OrderStatusCannotBeChangedException exception =
					assertThrows(OrderStatusCannotBeChangedException.class, sut::place);

			assertEquals(exception.getMessage(), ErrorMessages.Orders.orderStatusCannotBeChanged(sut.id(), OrderStatus.PLACED, OrderStatus.PLACED));
		}
	}

	@Nested
	@DisplayName("Order#markAsReady tests")
	class OrderMarkAsReadyTests {

		@Test
		void givenPaidOrderMarkAsReadyShouldChangeStatusAndReadyAtProperties() {
			Order sut = OrderTestBuilder
					.anExistingOrder()
					.withStatus(OrderStatus.PAID)
					.build();

			assertDoesNotThrow(sut::markAsReady);
			assertTrue(sut.isReady());
			assertNotNull(sut.readyAt());
		}

		@Test
		void givenOrderWithWrongStatusMarkAsReadyShouldThrowOrderStatusCannotBeChangedException() {
			Order sut = OrderTestBuilder
					.anExistingOrder()
					.withStatus(OrderStatus.PLACED)
					.build();

			assertThrows(OrderStatusCannotBeChangedException.class, sut::markAsReady);
			assertFalse(sut.isReady());
			assertNull(sut.readyAt());
		}
	}
}