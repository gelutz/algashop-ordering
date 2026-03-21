package com.lutz.algashop.ordering.domain.product.builder;

import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.product.Product;
import com.lutz.algashop.ordering.domain.product.ProductId;
import com.lutz.algashop.ordering.domain.product.ProductName;

public class ProductTestBuilder {
	public static final ProductId DEFAULT_PRODUCT_ID = new ProductId();

	private ProductTestBuilder() {
	}

	public static Product.ProductBuilder aProduct() {
		return Product.builder()
				.id(DEFAULT_PRODUCT_ID)
				      .productName(new ProductName("Test Product"))
				      .price(new Money("25.00"))
				      .inStock(true);
	}

	public static Product.ProductBuilder aProductUnavailable() {
		return Product.builder()
				.id(DEFAULT_PRODUCT_ID)
				.productName(new ProductName("Unavailable Product"))
				.price(new Money("25.00"))
				.inStock(false);
	}
}
