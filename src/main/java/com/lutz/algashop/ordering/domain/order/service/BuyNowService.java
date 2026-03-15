package com.lutz.algashop.ordering.domain.order.service;

import com.lutz.algashop.ordering.domain.DomainService;
import com.lutz.algashop.ordering.domain.commons.Quantity;
import com.lutz.algashop.ordering.domain.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.order.entity.Order;
import com.lutz.algashop.ordering.domain.order.entity.PaymentMethod;
import com.lutz.algashop.ordering.domain.order.vo.Billing;
import com.lutz.algashop.ordering.domain.product.Product;
import com.lutz.algashop.ordering.domain.order.shipping.Shipping;

@DomainService
public class BuyNowService {

	public Order buyNow(Product product, CustomerId customerId,
	                    Billing billing, Shipping shipping,
	                    Quantity quantity, PaymentMethod paymentMethod) {
		product.verifyIfIsInStock();

		Order order = Order.draft(customerId);

		order.changeBilling(billing);
		order.changeShipping(shipping);
		order.changePaymentMethod(paymentMethod);
		order.addItem(product, quantity);
		order.place();

		return order;
	}
}
