package com.lutz.algashop.ordering.domain.order.service;

import com.lutz.algashop.ordering.domain.DomainService;
import com.lutz.algashop.ordering.domain.order.entity.Order;
import com.lutz.algashop.ordering.domain.order.entity.PaymentMethod;
import com.lutz.algashop.ordering.domain.order.vo.Billing;
import com.lutz.algashop.ordering.domain.product.Product;
import com.lutz.algashop.ordering.domain.order.shipping.Shipping;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCart;
import com.lutz.algashop.ordering.domain.shoppingCart.exception.ShoppingCartCantProceedToCheckoutException;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartItem;

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
