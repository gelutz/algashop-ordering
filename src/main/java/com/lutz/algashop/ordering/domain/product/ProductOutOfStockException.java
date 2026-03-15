package com.lutz.algashop.ordering.domain.product;

import com.lutz.algashop.ordering.domain.DomainException;
import com.lutz.algashop.ordering.domain.commons.ErrorMessages;
import lombok.NonNull;

public class ProductOutOfStockException extends DomainException {
	public ProductOutOfStockException(@NonNull ProductId productId) {
		super(ErrorMessages.Products.productOutOfStock(productId));
	}
}
