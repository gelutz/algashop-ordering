package com.lutz.algashop.ordering.domain.product;

import com.lutz.algashop.ordering.domain.DomainException;
import lombok.NonNull;

public class ProductNotFoundException extends DomainException {
	public ProductNotFoundException(@NonNull ProductId productId) {
		super(ErrorMessages.productNotFound(productId));
	}
}
