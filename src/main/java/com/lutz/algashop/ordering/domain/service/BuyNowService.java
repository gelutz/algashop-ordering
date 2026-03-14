package com.lutz.algashop.ordering.domain.service;

import com.lutz.algashop.ordering.domain.entity.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.entity.order.Order;
import com.lutz.algashop.ordering.domain.entity.order.PaymentMethod;
import com.lutz.algashop.ordering.domain.entity.order.vo.Billing;
import com.lutz.algashop.ordering.domain.entity.order.vo.Product;
import com.lutz.algashop.ordering.domain.entity.order.vo.Quantity;
import com.lutz.algashop.ordering.domain.entity.order.vo.Shipping;
import com.lutz.algashop.ordering.domain.utils.annotations.DomainService;

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
