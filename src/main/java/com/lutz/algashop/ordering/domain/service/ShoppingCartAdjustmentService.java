package com.lutz.algashop.ordering.domain.service;

import com.lutz.algashop.ordering.domain.entity.order.vo.Money;
import com.lutz.algashop.ordering.domain.entity.order.vo.ProductId;

public interface ShoppingCartAdjustmentService {
	void adjustPrice(ProductId productId, Money updatedPrice);
	void changeAvailability(ProductId productId, boolean state);
}
