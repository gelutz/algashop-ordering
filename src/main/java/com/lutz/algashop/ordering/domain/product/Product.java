package com.lutz.algashop.ordering.domain.product;

import com.lutz.algashop.ordering.domain.commons.Money;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record Product(
		@NonNull ProductId id,
		@NonNull ProductName productName,
		@NonNull Money price,
		@NonNull Boolean inStock
) {

	public void verifyIfIsInStock() {
		if (!inStock()) {
			throw new ProductOutOfStockException(this.id());
		}
	}
}
