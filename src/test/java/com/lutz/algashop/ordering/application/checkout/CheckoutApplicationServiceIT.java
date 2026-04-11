package com.lutz.algashop.ordering.application.checkout;

import com.lutz.algashop.ordering.application.checkout.builder.CheckoutInputTestBuilder;
import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.commons.Quantity;
import com.lutz.algashop.ordering.domain.customer.Customers;
import com.lutz.algashop.ordering.domain.customer.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.domain.order.OrderId;
import com.lutz.algashop.ordering.domain.order.OrderPlacedEvent;
import com.lutz.algashop.ordering.domain.order.Orders;
import com.lutz.algashop.ordering.domain.order.shipping.ShippingCostService;
import com.lutz.algashop.ordering.infrastructure.listener.order.OrderEventListener;
import com.lutz.algashop.ordering.domain.shoppingCart.ShoppingCarts;
import com.lutz.algashop.ordering.domain.shoppingCart.builder.ShoppingCartItemTestBuilder;
import com.lutz.algashop.ordering.domain.shoppingCart.builder.ShoppingCartTestBuilder;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCart;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartItem;
import com.lutz.algashop.ordering.domain.shoppingCart.exception.ShoppingCartCantProceedToCheckoutException;
import com.lutz.algashop.ordering.domain.shoppingCart.exception.ShoppingCartNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@SpringBootTest
@Transactional
public class CheckoutApplicationServiceIT {

	@Autowired
	private CheckoutApplicationService sut;

	@Autowired
	private Customers customers;

	@Autowired
	private ShoppingCarts shoppingCarts;

	@Autowired
	private Orders orders;

	@MockitoBean
	private ShippingCostService shippingCostService;

	@MockitoSpyBean
	private OrderEventListener orderEventListener;

	@BeforeEach
	void setup() {
		if (!customers.exists(CustomerTestBuilder.DEFAULT_CUSTOMER_ID)) {
			customers.add(CustomerTestBuilder.aCustomer().build());
		}

		Mockito.when(shippingCostService.calculate(Mockito.any(ShippingCostService.CalculationRequest.class)))
				.thenReturn(new ShippingCostService.CalculationResult(
						new Money("10"),
						LocalDate.now().plusDays(3)
				));
	}

	@Test
	public void shouldCheckoutSuccessfully() {
		ShoppingCart shoppingCart = createCartWithItems(true);
		shoppingCarts.add(shoppingCart);

		CheckoutInput input = CheckoutInputTestBuilder.aCheckoutInput()
				.shoppingCartId(shoppingCart.id().value())
				.build();

		String orderId = sut.checkout(input);

		Assertions.assertNotNull(orderId);
		Assertions.assertTrue(orders.exists(new OrderId(orderId)));

		ShoppingCart updatedCart = shoppingCarts.ofId(shoppingCart.id()).orElseThrow();
		Assertions.assertTrue(updatedCart.isEmpty());
		Mockito.verify(orderEventListener, Mockito.times(1))
				.listen(Mockito.any(OrderPlacedEvent.class));
	}

	@Test
	public void shouldThrowWhenShoppingCartNotFound() {
		CheckoutInput input = CheckoutInputTestBuilder.aCheckoutInput()
				.shoppingCartId(UUID.randomUUID())
				.build();

		Assertions.assertThrows(ShoppingCartNotFoundException.class, () -> sut.checkout(input));
	}

	@Test
	public void shouldThrowWhenShoppingCartIsEmpty() {
		ShoppingCart emptyCart = ShoppingCartTestBuilder.fresh().build();
		shoppingCarts.add(emptyCart);

		CheckoutInput input = CheckoutInputTestBuilder.aCheckoutInput()
				.shoppingCartId(emptyCart.id().value())
				.build();

		Assertions.assertThrows(ShoppingCartCantProceedToCheckoutException.class, () -> sut.checkout(input));
	}

	@Test
	public void shouldThrowWhenCartContainsUnavailableItems() {
		ShoppingCart cart = createCartWithItems(false);
		shoppingCarts.add(cart);

		CheckoutInput input = CheckoutInputTestBuilder.aCheckoutInput()
				.shoppingCartId(cart.id().value())
				.build();

		Assertions.assertThrows(ShoppingCartCantProceedToCheckoutException.class, () -> sut.checkout(input));
	}

	private ShoppingCart createCartWithItems(boolean available) {
		ShoppingCartItem item = ShoppingCartItemTestBuilder.anExistingItem()
				.withAvailable(available)
				.withPrice(new Money("50"))
				.withQuantity(new Quantity(2))
				.withTotalAmount(new Money("100"))
				.build();

		return ShoppingCartTestBuilder.existing()
				.withItems(Set.of(item))
				.withTotalAmount(new Money("100"))
				.withTotalItems(new Quantity(2))
				.build();
	}
}
