package com.lutz.algashop.ordering.domain.exception;

import com.lutz.algashop.ordering.domain.entity.order.vo.ProductId;
import lombok.NonNull;

public class ProductOutOfStockException extends DomainException {
	public ProductOutOfStockException(@NonNull ProductId productId) {
		super(ErrorMessages.Products.productOutOfStock(productId));
	}
}
