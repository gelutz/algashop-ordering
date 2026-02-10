package com.lutz.algashop.ordering.domain.entity.shoppingCart;

import com.lutz.algashop.ordering.domain.entity.order.vo.Money;
import com.lutz.algashop.ordering.domain.entity.order.vo.ProductId;
import com.lutz.algashop.ordering.domain.entity.order.vo.ProductName;
import com.lutz.algashop.ordering.domain.entity.order.vo.Quantity;

public class ShoppingCartItem {
	public ShoppingCartItemId id;
	public ShoppingCartId shoppingCartId;
	public ProductId productId;
	public ProductName name;
	public Money price;
	public Quantity quantity;
	public Money totalAmount;
	public Boolean avaliable;

	void refresh() {
		throw new RuntimeException("Method not yet implemented.");
	}
	void changeQuantity(Quantity quantity) {
		throw new RuntimeException("Method not yet implemented.");
	}

	public ShoppingCartItemId id() {
		return id;
	}

	public ShoppingCartId shoppingCartId() {
		return shoppingCartId;
	}

	public ProductId productId() {
		return productId;
	}

	public ProductName name() {
		return name;
	}

	public Money price() {
		return price;
	}

	public Quantity quantity() {
		return quantity;
	}

	public Money totalAmount() {
		return totalAmount;
	}

	public Boolean avaliable() {
		return avaliable;
	}
}
