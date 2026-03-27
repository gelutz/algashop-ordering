package com.lutz.algashop.ordering.domain.shoppingCart.exception;

import com.lutz.algashop.ordering.domain.customer.CustomerId;
import com.lutz.algashop.ordering.domain.product.ProductId;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartId;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartItemId;

public class ErrorMessages {
	public static String cartDoesNotContainCartItem(ShoppingCartId id, ShoppingCartItemId itemid) {
		return shoppingCartMessageWrapper(id, String.format("The shopping cart does not contain this item: [%s]", itemid));
	}

	public static String cartDoesNotContainProduct(ShoppingCartId id, ProductId productId) {
		return shoppingCartMessageWrapper(id, String.format("The shopping cart does not contain this product: [%s]", productId));
	}

	private static String shoppingCartMessageWrapper(ShoppingCartId id, String message) {
		return String.format("ShoppingCart [%s]: %s", id, message);
	}

	public static String shoppingCartProductsDontMatch(ShoppingCartItemId id, ProductId that, ProductId other) {
		return shoppingCartItemMessageWrapper(
				id,
				String.format("Cannot refresh. Given Product id %s does not match Shopping cart item id %s", that, other)
		                                     );

	}


	public static String shoppingCartNotFound(ShoppingCartId id) {
		return shoppingCartMessageWrapper(id, "Shopping cart not found.");
	}

	public static String customerAlreadyHasShoppingCart(CustomerId customerId) {
		return String.format("Customer [%s] already has an active shopping cart.", customerId);
	}

	private static String shoppingCartItemMessageWrapper(ShoppingCartItemId id, String message) {
		return String.format("ShoppingCartItem [%s]: %s", id, message);
		}
}
