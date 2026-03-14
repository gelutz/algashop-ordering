package com.lutz.algashop.ordering.domain.service;

import com.lutz.algashop.ordering.domain.entity.order.Order;
import com.lutz.algashop.ordering.domain.entity.order.PaymentMethod;
import com.lutz.algashop.ordering.domain.entity.order.vo.Billing;
import com.lutz.algashop.ordering.domain.entity.order.vo.Product;
import com.lutz.algashop.ordering.domain.entity.order.vo.Shipping;
import com.lutz.algashop.ordering.domain.entity.shoppingCart.ShoppingCart;
import com.lutz.algashop.ordering.domain.entity.shoppingCart.ShoppingCartItem;
import com.lutz.algashop.ordering.domain.exception.shoppingCart.ShoppingCartCantProceedToCheckoutException;
import com.lutz.algashop.ordering.domain.utils.annotations.DomainService;

@DomainService
public class CheckoutService {

	public Order checkout(ShoppingCart shoppingCart, Billing billing, Shipping shipping, PaymentMethod paymentMethod) {
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

		order.place();
		shoppingCart.empty();

		return order;
	}
}
