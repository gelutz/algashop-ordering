package com.lutz.algashop.ordering.domain.order.entity;

import com.lutz.algashop.ordering.domain.order.builder.OrderTestBuilder;
import com.lutz.algashop.ordering.domain.order.exception.ErrorMessages;
import com.lutz.algashop.ordering.domain.order.exception.OrderStatusCannotBeChangedException;
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

			assertEquals(exception.getMessage(), ErrorMessages.orderStatusCannotBeChanged(sut.id(), OrderStatus.PLACED, OrderStatus.PLACED));
		}
	}

	@Nested
	@DisplayName("Order#markAsReady tests")
	class OrderMarkAsReadyTests {

		@Test
		void givenPaidOrderMarkAsReadyShouldChangeStatusAndReadyAtProperties() {
			Order sut = OrderTestBuilder
					.anExistingOrder()
					.build();
			sut.place();
			sut.markAsPaid();

			assertDoesNotThrow(sut::markAsReady);
			assertTrue(sut.isReady());
			assertNotNull(sut.readyAt());
		}

		@Test
		void givenOrderWithWrongStatusMarkAsReadyShouldThrowOrderStatusCannotBeChangedException() {
			Order sut = OrderTestBuilder
					.anExistingOrder()
					.build();

			assertThrows(OrderStatusCannotBeChangedException.class, sut::markAsReady);
			assertFalse(sut.isReady());
			assertNull(sut.readyAt());

			sut = OrderTestBuilder
					.anExistingOrder()
					.build();
			sut.cancel();

			assertThrows(OrderStatusCannotBeChangedException.class, sut::markAsReady);
			assertFalse(sut.isReady());
			assertNull(sut.readyAt());

			sut = OrderTestBuilder
					.anExistingOrder()
					.build();
			sut.place();

			assertThrows(OrderStatusCannotBeChangedException.class, sut::markAsReady);
			assertFalse(sut.isReady());
			assertNull(sut.readyAt());
		}
	}

	@Nested
	@DisplayName("Order#cancel tests")
	class OrderCancelTests {

		@Test
		void givenCanceledOrderCancelShouldThrowOrderStatusCannotBeChangedException() {
			Order sut = OrderTestBuilder
					.anExistingOrder()
					.build();
			sut.cancel();

			assertThrows(OrderStatusCannotBeChangedException.class, sut::cancel);
		}

		@Test
		void givenOrderWithAnyStatusCancelShouldWork() {
			Order sut = OrderTestBuilder
					.anExistingOrder()
					.build();

			assertDoesNotThrow(sut::cancel);
			assertTrue(sut.isCanceled());
			assertNotNull(sut.canceledAt());

			sut = OrderTestBuilder
					.anExistingOrder()
					.build();
			sut.place();
			sut.markAsPaid();
			sut.markAsReady();

			assertDoesNotThrow(sut::cancel);
			assertTrue(sut.isCanceled());
			assertNotNull(sut.canceledAt());

			sut = OrderTestBuilder
					.anExistingOrder()
					.build();
			sut.place();

			assertDoesNotThrow(sut::cancel);
			assertTrue(sut.isCanceled());
			assertNotNull(sut.canceledAt());

			sut = OrderTestBuilder
					.anExistingOrder()
					.build();
			sut.place();
			sut.markAsPaid();

			assertDoesNotThrow(sut::cancel);
			assertTrue(sut.isCanceled());
			assertNotNull(sut.canceledAt());
		}
	}
}