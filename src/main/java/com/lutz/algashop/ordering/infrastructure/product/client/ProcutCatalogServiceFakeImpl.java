package com.lutz.algashop.ordering.infrastructure.product.client;

import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.product.Product;
import com.lutz.algashop.ordering.domain.product.ProductId;
import com.lutz.algashop.ordering.domain.product.ProductName;
import com.lutz.algashop.ordering.domain.product.ProductCatalogService;

import java.util.Optional;

public class ProcutCatalogServiceFakeImpl implements ProductCatalogService {

	@Override
	public Optional<Product> ofId(ProductId id) {
		return Optional.of(Product.builder()
		                          .id(id)
		                          .productName(new ProductName("Some name"))
		                          .price(new Money("5"))
		                          .inStock(true)
		                          .build());
	}
}
