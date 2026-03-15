package com.lutz.algashop.ordering.domain.product;


import java.util.Optional;

// has similar functionalities to repositories but since we cant create repos for vos, services are the next best thing
public interface ProductCatalogService {
	Optional<Product> ofId(ProductId id);
}
