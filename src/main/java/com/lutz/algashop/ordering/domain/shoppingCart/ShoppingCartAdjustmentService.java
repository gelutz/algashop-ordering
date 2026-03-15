package com.lutz.algashop.ordering.domain.shoppingCart;

import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.product.ProductId;

public interface ShoppingCartAdjustmentService {
	void adjustPrice(ProductId productId, Money updatedPrice);
	void changeAvailability(ProductId productId, boolean state);
}
