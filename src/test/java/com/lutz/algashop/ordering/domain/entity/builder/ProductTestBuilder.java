package com.lutz.algashop.ordering.domain.entity.builder;

import com.lutz.algashop.ordering.domain.entity.order.vo.Money;
import com.lutz.algashop.ordering.domain.entity.order.vo.Product;
import com.lutz.algashop.ordering.domain.entity.order.vo.ProductId;
import com.lutz.algashop.ordering.domain.entity.order.vo.ProductName;

public class ProductTestBuilder {
	private ProductTestBuilder() {

	}

	public static Product.ProductBuilder aProduct() {
		return Product.builder()
				.productId(new ProductId())
				      .productName(new ProductName("Test Product"))
				      .price(new Money("25.00"))
				      .inStock(true);
	}
}
