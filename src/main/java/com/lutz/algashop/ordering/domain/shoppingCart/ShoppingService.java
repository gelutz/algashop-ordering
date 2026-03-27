package com.lutz.algashop.ordering.domain.shoppingCart;

import com.lutz.algashop.ordering.domain.DomainService;
import com.lutz.algashop.ordering.domain.customer.CustomerId;
import com.lutz.algashop.ordering.domain.customer.CustomerNotFoundException;
import com.lutz.algashop.ordering.domain.customer.Customers;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCart;
import com.lutz.algashop.ordering.domain.shoppingCart.exception.CustomerAlreadyHasShoppingCartException;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class ShoppingService {
	private final Customers customers;
	private final ShoppingCarts shoppingCarts;

	public ShoppingCart startShopping(CustomerId customerId) {
		customers.ofId(customerId).orElseThrow(CustomerNotFoundException::new);

		shoppingCarts.ofCustomer(customerId).ifPresent(cart -> {
			throw new CustomerAlreadyHasShoppingCartException(customerId);
		});

		return ShoppingCart.startShopping(customerId);
	}
}
