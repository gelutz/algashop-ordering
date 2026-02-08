package com.lutz.algashop.ordering.domain.exception.order;

import com.lutz.algashop.ordering.domain.entity.order.vo.ProductId;
import com.lutz.algashop.ordering.domain.exception.DomainException;
import com.lutz.algashop.ordering.domain.exception.ErrorMessages;
import lombok.NonNull;

public class ProductOutOfStockException extends DomainException {
	public ProductOutOfStockException(@NonNull ProductId productId) {
		super(ErrorMessages.Products.productOutOfStock(productId));
	}
}
