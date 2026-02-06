package com.lutz.algashop.ordering.domain.entity.customer.order;

import com.lutz.algashop.ordering.domain.entity.order.OrderStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderStatusTest {


	@Test
	void givenValidNewStatusCanChangeShouldReturnTrue() {
		Assertions.assertThat(OrderStatus.DRAFT.canChangeTo(OrderStatus.PLACED)).isTrue();
		Assertions.assertThat(OrderStatus.PLACED.canChangeTo(OrderStatus.PAID)).isTrue();
		Assertions.assertThat(OrderStatus.PAID.canChangeTo(OrderStatus.READY)).isTrue();


		Assertions.assertThat(OrderStatus.DRAFT.canChangeTo(OrderStatus.CANCELED)).isTrue();
		Assertions.assertThat(OrderStatus.PLACED.canChangeTo(OrderStatus.CANCELED)).isTrue();
		Assertions.assertThat(OrderStatus.PAID.canChangeTo(OrderStatus.CANCELED)).isTrue();
		Assertions.assertThat(OrderStatus.READY.canChangeTo(OrderStatus.CANCELED)).isTrue();
	}

	@Test
	void givenInvalidNewStatusCannotChangeShouldReturnTrue() {
		Assertions.assertThat(OrderStatus.PLACED.cannotChangeTo(OrderStatus.DRAFT)).isTrue();
		Assertions.assertThat(OrderStatus.DRAFT.cannotChangeTo(OrderStatus.PAID)).isTrue();
		Assertions.assertThat(OrderStatus.PAID.cannotChangeTo(OrderStatus.PAID)).isTrue();
		Assertions.assertThat(OrderStatus.READY.cannotChangeTo(OrderStatus.PAID)).isTrue();
	}
}