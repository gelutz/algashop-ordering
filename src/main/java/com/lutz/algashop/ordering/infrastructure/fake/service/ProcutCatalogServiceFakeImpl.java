package com.lutz.algashop.ordering.infrastructure.fake.service;

import com.lutz.algashop.ordering.domain.entity.order.vo.Money;
import com.lutz.algashop.ordering.domain.entity.order.vo.Product;
import com.lutz.algashop.ordering.domain.entity.order.vo.ProductId;
import com.lutz.algashop.ordering.domain.entity.order.vo.ProductName;
import com.lutz.algashop.ordering.domain.service.ProductCatalogService;

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
