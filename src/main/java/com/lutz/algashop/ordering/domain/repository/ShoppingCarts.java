package com.lutz.algashop.ordering.domain.repository;

import com.lutz.algashop.ordering.domain.entity.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.entity.shoppingCart.ShoppingCart;
import com.lutz.algashop.ordering.domain.entity.shoppingCart.ShoppingCartId;

import java.util.Optional;

public interface ShoppingCarts extends RemoveCapableRepository<ShoppingCart, ShoppingCartId> {
	Optional<ShoppingCart> ofCustomer(CustomerId customerId);
}
