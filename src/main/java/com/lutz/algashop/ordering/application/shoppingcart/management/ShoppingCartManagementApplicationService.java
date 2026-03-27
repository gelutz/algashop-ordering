package com.lutz.algashop.ordering.application.shoppingcart.management;

import com.lutz.algashop.ordering.domain.commons.Quantity;
import com.lutz.algashop.ordering.domain.customer.CustomerId;
import com.lutz.algashop.ordering.domain.product.Product;
import com.lutz.algashop.ordering.domain.product.ProductCatalogService;
import com.lutz.algashop.ordering.domain.product.ProductId;
import com.lutz.algashop.ordering.domain.product.ProductNotFoundException;
import com.lutz.algashop.ordering.domain.shoppingCart.ShoppingCarts;
import com.lutz.algashop.ordering.domain.shoppingCart.ShoppingService;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCart;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartId;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartItemId;
import com.lutz.algashop.ordering.domain.shoppingCart.exception.ShoppingCartNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingCartManagementApplicationService {
	private final ShoppingService shoppingService;
	private final ShoppingCarts shoppingCarts;
	private final ProductCatalogService productCatalogService;

	@Transactional
	public void addItem(@NonNull ShoppingCartItemInput input) {
		ShoppingCartId shoppingCartId = new ShoppingCartId(input.getShoppingCartId());
		ShoppingCart shoppingCart = findShoppingCart(shoppingCartId);

		ProductId productId = new ProductId(input.getProductId());
		Product product = productCatalogService.ofId(productId)
				.orElseThrow(() -> new ProductNotFoundException(productId));

		shoppingCart.addItem(product, new Quantity(input.getQuantity()));
		shoppingCarts.add(shoppingCart);
	}

	@Transactional
	public UUID createNew(@NonNull UUID rawCustomerId) {
		CustomerId customerId = new CustomerId(rawCustomerId);
		ShoppingCart shoppingCart = shoppingService.startShopping(customerId);
		shoppingCarts.add(shoppingCart);
		return shoppingCart.id().value();
	}

	@Transactional
	public void removeItem(@NonNull UUID rawShoppingCartId, @NonNull UUID rawShoppingCartItemId) {
		ShoppingCartId shoppingCartId = new ShoppingCartId(rawShoppingCartId);
		ShoppingCart shoppingCart = findShoppingCart(shoppingCartId);

		shoppingCart.removeItem(new ShoppingCartItemId(rawShoppingCartItemId));
		shoppingCarts.add(shoppingCart);
	}

	@Transactional
	public void empty(@NonNull UUID rawShoppingCartId) {
		ShoppingCartId shoppingCartId = new ShoppingCartId(rawShoppingCartId);
		ShoppingCart shoppingCart = findShoppingCart(shoppingCartId);

		shoppingCart.empty();
		shoppingCarts.add(shoppingCart);
	}

	@Transactional
	public void delete(@NonNull UUID rawShoppingCartId) {
		ShoppingCartId shoppingCartId = new ShoppingCartId(rawShoppingCartId);
		ShoppingCart shoppingCart = findShoppingCart(shoppingCartId);

		shoppingCarts.remove(shoppingCart);
	}

	private ShoppingCart findShoppingCart(ShoppingCartId id) {
		return shoppingCarts.ofId(id)
				.orElseThrow(() -> new ShoppingCartNotFoundException(id));
	}
}
