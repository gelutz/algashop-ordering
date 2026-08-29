package com.lutz.algashop.ordering.infrastructure.product.client.http;

import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.product.Product;
import com.lutz.algashop.ordering.domain.product.ProductCatalogService;
import com.lutz.algashop.ordering.domain.product.ProductId;
import com.lutz.algashop.ordering.domain.product.ProductName;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "algashop.integrations.product-catalog.provider", havingValue = "HTTP")
public class ProductCatalogServiceHttpImpl implements ProductCatalogService {
	private final ProductCatalogAPIClient productClient;


	@Override
	public Optional<Product> ofId(ProductId id) {
		ProductResponse product = productClient.getById(id.value());
		return Optional.of(
				Product.builder()
				       .id(new ProductId(product.getId()))
                       .productName(new ProductName(product.getName()))
                       .price(new Money(product.getSalePrice()))
                       .inStock(product.getInStock())
                       .build()
		);
	}
}
