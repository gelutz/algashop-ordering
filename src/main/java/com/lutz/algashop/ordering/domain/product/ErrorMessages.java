package com.lutz.algashop.ordering.domain.product;

public class ErrorMessages {
	public static String productOutOfStock(ProductId productId) {
		return productMessageWrapper(productId, "The product is out of stock.");
	}

	private static String productMessageWrapper(ProductId productId, String message) {
		return String.format("Product [%s]: %s", productId, message);
	}
}
