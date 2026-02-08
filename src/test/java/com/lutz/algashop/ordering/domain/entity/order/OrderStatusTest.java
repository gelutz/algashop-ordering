package com.lutz.algashop.ordering.domain.entity.order;

import com.lutz.algashop.ordering.domain.entity.order.OrderStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
}