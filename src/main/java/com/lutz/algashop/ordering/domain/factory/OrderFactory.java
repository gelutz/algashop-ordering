package com.lutz.algashop.ordering.domain.factory;

import com.lutz.algashop.ordering.domain.entity.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.entity.order.Order;
import com.lutz.algashop.ordering.domain.entity.order.PaymentMethod;
import com.lutz.algashop.ordering.domain.entity.order.vo.Billing;
import com.lutz.algashop.ordering.domain.entity.order.vo.Product;
import com.lutz.algashop.ordering.domain.entity.order.vo.Quantity;
import com.lutz.algashop.ordering.domain.entity.order.vo.Shipping;
import lombok.NonNull;

public class OrderFactory {
	private OrderFactory() {}

	public static Order filled(
		@NonNull CustomerId customerId,
		@NonNull Shipping shipping,
		@NonNull Billing billing,
		@NonNull PaymentMethod paymentMethod,
		@NonNull Product product,
		@NonNull Quantity quantity
	                          ) {
		Order order = Order.draft(customerId);
		order.changeShipping(shipping);
		order.changeBilling(billing);
		order.changePaymentMethod(paymentMethod);

		order.addItem(product, quantity);

		return order;
	}
}
