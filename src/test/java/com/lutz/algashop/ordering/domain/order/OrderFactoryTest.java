package com.lutz.algashop.ordering.domain.order;

import com.lutz.algashop.ordering.domain.commons.Quantity;
import com.lutz.algashop.ordering.domain.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.order.builder.OrderTestBuilder;
import com.lutz.algashop.ordering.domain.order.entity.Order;
import com.lutz.algashop.ordering.domain.order.entity.PaymentMethod;
import com.lutz.algashop.ordering.domain.order.shipping.Shipping;
import com.lutz.algashop.ordering.domain.order.vo.Billing;
import com.lutz.algashop.ordering.domain.product.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderFactoryTest {
	CustomerId customerId = new CustomerId();
	Shipping shipping = OrderTestBuilder.aShipping().build();
	Billing billing = OrderTestBuilder.aBilling().build();
	Product product = OrderTestBuilder.aProduct().build();
	PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;
	@Test
	void shouldGenerateOrderThatCanBePlaced() {
		Order order = OrderFactory.filled(
				customerId,
				shipping,
				billing,
				paymentMethod,
				product,
				new Quantity(1)
		                                 );

		Assertions.assertEquals(customerId, order.customerId());
		Assertions.assertEquals(shipping, order.shipping());
		Assertions.assertEquals(billing, order.billing());
		Assertions.assertEquals(paymentMethod, order.paymentMethod());
		Assertions.assertEquals(product.id(), order.items().iterator().next().productId());
		Assertions.assertEquals(product.price(), order.items().iterator().next().price());
		Assertions.assertEquals(new Quantity(1), order.items().iterator().next().quantity());

		Assertions.assertDoesNotThrow(order::place);
		Assertions.assertTrue(order.isPlaced());
	}
}