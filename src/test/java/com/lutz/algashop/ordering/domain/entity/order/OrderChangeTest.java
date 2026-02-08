package com.lutz.algashop.ordering.domain.entity.order;

import com.lutz.algashop.ordering.domain.entity.builder.OrderTestBuilder;
import com.lutz.algashop.ordering.domain.entity.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.entity.order.vo.OrderItemId;
import com.lutz.algashop.ordering.domain.entity.order.vo.Quantity;
import com.lutz.algashop.ordering.domain.exception.order.OrderCannotBeEditedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderChangeTest {
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
