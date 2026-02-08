package com.lutz.algashop.ordering.domain.entity.order.vo;

import com.lutz.algashop.ordering.domain.exception.order.ProductOutOfStockException;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record Product(
		@NonNull ProductId productId,
		@NonNull ProductName productName,
		@NonNull Money price,
		@NonNull Boolean inStock
) {

	public void verifyIfIsInStock() {
		if (!inStock()) {
			throw new ProductOutOfStockException(this.productId());
		}
	}
}
