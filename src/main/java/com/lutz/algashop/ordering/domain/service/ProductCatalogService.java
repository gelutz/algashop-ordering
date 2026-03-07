package com.lutz.algashop.ordering.domain.service;


import com.lutz.algashop.ordering.domain.entity.order.vo.Product;
import com.lutz.algashop.ordering.domain.entity.order.vo.ProductId;

import java.util.Optional;

// has similar functionalities to repositories but since we cant create repos for vos, services are the next best thing
public interface ProductCatalogService {
	Optional<Product> ofId(ProductId id);
}
