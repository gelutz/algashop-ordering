package com.lutz.algashop.ordering.infrastructure.persistence.shoppingCart;

import com.lutz.algashop.ordering.application.shoppingcart.query.ShoppingCartOutput;
import com.lutz.algashop.ordering.application.shoppingcart.query.ShoppingCartQueryService;
import com.lutz.algashop.ordering.application.utility.Mapper;
import com.lutz.algashop.ordering.domain.shoppingCart.exception.ShoppingCartNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoppingCartQueryServiceImpl implements ShoppingCartQueryService {

	private final ShoppingCartPersistenceEntityRepository repository;
	private final Mapper mapper;

	@Override
	public ShoppingCartOutput findById(UUID shoppingCartId) {
		return repository.findById(shoppingCartId)
				.map(e -> mapper.convert(e, ShoppingCartOutput.class))
				.orElseThrow(ShoppingCartNotFoundException::new);
	}

	@Override
	public ShoppingCartOutput findByCustomerId(UUID customerId) {
		return repository.findByCustomerId(customerId)
				.map(e -> mapper.convert(e, ShoppingCartOutput.class))
				.orElseThrow(ShoppingCartNotFoundException::new);
	}
}
