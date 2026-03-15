package com.lutz.algashop.ordering.infrastructure.persistence.shoppingCart;

import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.product.ProductId;
import com.lutz.algashop.ordering.domain.shoppingCart.ShoppingCartAdjustmentService;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShoppingCartUpdateProvider implements ShoppingCartAdjustmentService {

	private final ShoppingCartPersistenceEntityRepository persistenceRepository;

	@Override
	@Transactional
	public void adjustPrice(@NonNull ProductId productId, @NonNull Money updatedPrice) {
		persistenceRepository.updateItemPrice(productId.value(), updatedPrice.value());
		persistenceRepository.recalculateTotalsForCartsWithProduct(productId.value());
	}

	@Override
	@Transactional
	public void changeAvailability(ProductId productId, boolean state) {
		persistenceRepository.updateItemAvailability(productId.value(), state);
		persistenceRepository.recalculateTotalsForCartsWithProduct(productId.value());
	}
}
