package com.lutz.algashop.ordering.domain.order;

import com.lutz.algashop.ordering.domain.DomainService;
import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.commons.Quantity;
import com.lutz.algashop.ordering.domain.customer.Customer;
import com.lutz.algashop.ordering.domain.order.entity.Order;
import com.lutz.algashop.ordering.domain.order.entity.PaymentMethod;
import com.lutz.algashop.ordering.domain.order.shipping.Shipping;
import com.lutz.algashop.ordering.domain.order.specification.CustomerHasFreeShippingSpecification;
import com.lutz.algashop.ordering.domain.product.Product;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class BuyNowService {

	private final CustomerHasFreeShippingSpecification customerHasFreeShippingSpecification;

	public Order buyNow(
			Product product, Customer customer,
			Billing billing, Shipping shipping,
			Quantity quantity, PaymentMethod paymentMethod
	) {
		product.verifyIfIsInStock();

		Order order = Order.draft(customer.id());

		order.changeBilling(billing);
		order.changePaymentMethod(paymentMethod);
		order.addItem(product, quantity);

		if (!hasFreeShipping(customer)) {
			order.changeShipping(shipping);
		} else {
			Shipping freeShipping = shipping.toBuilder()
			                                .cost(Money.ZERO)
			                                .build();
			order.changeShipping(freeShipping);
		}

		order.place();

		return order;
	}

	private boolean hasFreeShipping(Customer customer) {
		return customerHasFreeShippingSpecification.isSatisfiedBy(customer);
	}
}
