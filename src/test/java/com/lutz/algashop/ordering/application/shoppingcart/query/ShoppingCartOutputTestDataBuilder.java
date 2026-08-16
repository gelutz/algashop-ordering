package com.lutz.algashop.ordering.application.shoppingcart.query;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShoppingCartOutputTestDataBuilder {

	public static ShoppingCartOutput existing() {
		return existing(UUID.randomUUID(), UUID.randomUUID());
	}

	public static ShoppingCartOutput existing(UUID shoppingCartId, UUID customerId) {
		ShoppingCartOutput output = new ShoppingCartOutput();
		output.setId(shoppingCartId);
		output.setCustomerId(customerId);
		output.setTotalItems(2);
		output.setTotalAmount(new BigDecimal("39.98"));
		output.setItems(items(shoppingCartId));
		return output;
	}

	private static List<ShoppingCartItemOutput> items(UUID shoppingCartId) {
		List<ShoppingCartItemOutput> items = new ArrayList<>();
		items.add(item());
		return items;
	}

	public static ShoppingCartItemOutput item() {
		ShoppingCartItemOutput item = new ShoppingCartItemOutput();
		item.setId(UUID.randomUUID());
		item.setProductId(UUID.randomUUID());
		item.setName("Notebook Dive Gamer X11");
		item.setPrice(new BigDecimal("19.99"));
		item.setQuantity(2);
		item.setTotalAmount(new BigDecimal("39.98"));
		item.setAvailable(true);
		return item;
	}
}
