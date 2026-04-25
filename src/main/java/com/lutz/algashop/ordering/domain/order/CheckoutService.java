package com.lutz.algashop.ordering.domain.order;

import com.lutz.algashop.ordering.domain.DomainService;
import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.customer.Customer;
import com.lutz.algashop.ordering.domain.order.entity.Order;
import com.lutz.algashop.ordering.domain.order.entity.PaymentMethod;
import com.lutz.algashop.ordering.domain.order.shipping.Shipping;
import com.lutz.algashop.ordering.domain.order.specification.CustomerHasFreeShippingSpecification;
import com.lutz.algashop.ordering.domain.product.Product;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCart;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartItem;
import com.lutz.algashop.ordering.domain.shoppingCart.exception.ShoppingCartCantProceedToCheckoutException;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class CheckoutService {

	private final CustomerHasFreeShippingSpecification customerHasFreeShipping;

	public Order checkout(Customer customer, ShoppingCart shoppingCart, Billing billing, Shipping shipping, PaymentMethod paymentMethod) {
		if (shoppingCart.isEmpty()) {
			throw new ShoppingCartCantProceedToCheckoutException(shoppingCart.id(), "The shopping cart is empty.");
		}

		if (shoppingCart.containsUnavailableItems()) {
			throw new ShoppingCartCantProceedToCheckoutException(shoppingCart.id(), "The shopping cart contains unavailable items.");
		}

		Order order = Order.draft(shoppingCart.customerId());

		order.changeBilling(billing);
		order.changeShipping(shipping);
		order.changePaymentMethod(paymentMethod);

		for (ShoppingCartItem item : shoppingCart.items()) {
			Product product = Product.builder()
					.id(item.productId())
					.productName(item.name())
					.price(item.price())
					.inStock(item.available())
					.build();

			order.addItem(product, item.quantity());
		}

		if (!hasFreeShipping(customer)) {
			order.changeShipping(shipping);
		} else {
			Shipping freeShipping = shipping.toBuilder()
			                                .cost(Money.ZERO)
			                                .build();
			order.changeShipping(freeShipping);
		}

		order.place();
		shoppingCart.empty();

		return order;
	}

	private boolean hasFreeShipping(Customer customer) {
		return customerHasFreeShipping.isSatisfiedBy(customer);
	}
}
