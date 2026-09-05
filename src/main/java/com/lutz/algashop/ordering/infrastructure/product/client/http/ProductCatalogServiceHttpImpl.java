package com.lutz.algashop.ordering.infrastructure.product.client.http;

import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.product.Product;
import com.lutz.algashop.ordering.domain.product.ProductCatalogService;
import com.lutz.algashop.ordering.domain.product.ProductId;
import com.lutz.algashop.ordering.domain.product.ProductName;
import com.lutz.algashop.ordering.presentation.BadGatewayException;
import com.lutz.algashop.ordering.presentation.GatewayTimeoutException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "algashop.integrations.product-catalog.provider", havingValue = "HTTP")
public class ProductCatalogServiceHttpImpl implements ProductCatalogService {
	private final ProductCatalogAPIClient productClient;


	@Override
	public Optional<Product> ofId(ProductId id) {
		ProductResponse product;
		try {
			product = productClient.getById(id.value());
		} catch (ResourceAccessException e) {
			throw new GatewayTimeoutException("Product Catalog API Timeout", e);
		} catch (HttpClientErrorException.NotFound e) {
			return Optional.empty();
		} catch (HttpClientErrorException e) {
			throw new BadGatewayException("Product Catalog API Bad Gateway", e);
		}
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
