package com.lutz.algashop.ordering.infrastructure.listener.shoppingcart;

import com.lutz.algashop.ordering.domain.shoppingCart.ShoppingCartCreatedEvent;
import com.lutz.algashop.ordering.domain.shoppingCart.ShoppingCartEmptiedEvent;
import com.lutz.algashop.ordering.domain.shoppingCart.ShoppingCartItemAddedEvent;
import com.lutz.algashop.ordering.domain.shoppingCart.ShoppingCartItemRemovedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ShoppingCartEventListener {

	@EventListener
	public void listen(ShoppingCartCreatedEvent event) {

	}

	@EventListener
	public void listen(ShoppingCartEmptiedEvent event) {

	}

	@EventListener
	public void listen(ShoppingCartItemAddedEvent event) {

	}

	@EventListener
	public void listen(ShoppingCartItemRemovedEvent event) {

	}

}
