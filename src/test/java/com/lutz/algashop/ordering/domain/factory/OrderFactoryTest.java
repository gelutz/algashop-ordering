package com.lutz.algashop.ordering.domain.factory;

import com.lutz.algashop.ordering.domain.entity.builder.OrderTestBuilder;
import com.lutz.algashop.ordering.domain.entity.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.entity.order.Order;
import com.lutz.algashop.ordering.domain.entity.order.PaymentMethod;
import com.lutz.algashop.ordering.domain.entity.order.vo.Billing;
import com.lutz.algashop.ordering.domain.entity.order.vo.Product;
import com.lutz.algashop.ordering.domain.entity.order.vo.Quantity;
import com.lutz.algashop.ordering.domain.entity.order.vo.Shipping;
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
		Assertions.assertEquals(product.productId(), order.items().iterator().next().productId());
		Assertions.assertEquals(product.price(), order.items().iterator().next().price());
		Assertions.assertEquals(new Quantity(1), order.items().iterator().next().quantity());

		Assertions.assertDoesNotThrow(order::place);
		Assertions.assertTrue(order.isPlaced());
	}
}