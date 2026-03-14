package com.lutz.algashop.ordering.domain.service;

import com.lutz.algashop.ordering.domain.entity.builder.OrderTestBuilder;
import com.lutz.algashop.ordering.domain.entity.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.entity.order.Order;
import com.lutz.algashop.ordering.domain.entity.order.OrderStatus;
import com.lutz.algashop.ordering.domain.entity.order.PaymentMethod;
import com.lutz.algashop.ordering.domain.entity.order.vo.*;
import com.lutz.algashop.ordering.domain.entity.shoppingCart.ShoppingCart;
import com.lutz.algashop.ordering.domain.exception.shoppingCart.ShoppingCartCantProceedToCheckoutException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CheckoutServiceTest {

	private final CheckoutService sut = new CheckoutService();

	private final CustomerId customerId = new CustomerId();
	private final Billing billing = OrderTestBuilder.aBilling().build();
	private final Shipping shipping = OrderTestBuilder.aShipping().build();
	private final PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;

	private final Product productA = Product.builder()
			.id(new ProductId())
			.productName(new ProductName("Product A"))
			.price(new Money("25.00"))
			.inStock(true)
			.build();

	private final Product productB = Product.builder()
			.id(new ProductId())
			.productName(new ProductName("Product B"))
			.price(new Money("50.00"))
			.inStock(true)
			.build();

	@Nested
	@DisplayName("checkout com carrinho válido")
	class ValidCartCheckout {

		@Test
		@DisplayName("deve retornar pedido com status PLACED")
		void shouldReturnPlacedOrder() {
			ShoppingCart cart = ShoppingCart.startShopping(customerId);
			cart.addItem(productA, new Quantity(2));
			cart.addItem(productB, new Quantity(1));

			Order order = sut.checkout(cart, billing, shipping, paymentMethod);

			assertThat(order).isNotNull();
			assertThat(order.status()).isEqualTo(OrderStatus.PLACED);
			assertThat(order.customerId()).isEqualTo(customerId);
			assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
			assertThat(order.billing()).isEqualTo(billing);
			assertThat(order.shipping()).isEqualTo(shipping);
		}

		@Test
		@DisplayName("deve transferir itens corretamente para o pedido")
		void shouldTransferItemsCorrectly() {
			ShoppingCart cart = ShoppingCart.startShopping(customerId);
			cart.addItem(productA, new Quantity(2));
			cart.addItem(productB, new Quantity(1));

			Order order = sut.checkout(cart, billing, shipping, paymentMethod);

			assertThat(order.items()).hasSize(2);
			assertThat(order.itemsAmount()).isEqualTo(new Quantity(3));

			Money expectedItemsTotal = new Money("100.00"); // 25*2 + 50*1
			Money expectedTotal = expectedItemsTotal.add(shipping.cost());
			assertThat(order.totalAmount()).isEqualTo(expectedTotal);
		}

		@Test
		@DisplayName("deve esvaziar o carrinho após checkout")
		void shouldEmptyShoppingCart() {
			ShoppingCart cart = ShoppingCart.startShopping(customerId);
			cart.addItem(productA, new Quantity(1));

			sut.checkout(cart, billing, shipping, paymentMethod);

			assertThat(cart.isEmpty()).isTrue();
			assertThat(cart.items()).isEmpty();
		}
	}

	@Nested
	@DisplayName("checkout com itens indisponíveis")
	class UnavailableItems {

		@Test
		@DisplayName("deve lançar exceção quando carrinho contém itens indisponíveis")
		void shouldThrowException() {
			ShoppingCart cart = ShoppingCart.startShopping(customerId);
			cart.addItem(productA, new Quantity(1));

			Product unavailableProduct = Product.builder()
					.id(productA.id())
					.productName(productA.productName())
					.price(productA.price())
					.inStock(false)
					.build();
			cart.refreshItem(unavailableProduct);

			assertThatThrownBy(() -> sut.checkout(cart, billing, shipping, paymentMethod))
					.isInstanceOf(ShoppingCartCantProceedToCheckoutException.class);
		}

		@Test
		@DisplayName("não deve esvaziar o carrinho quando checkout falha")
		void shouldNotEmptyCart() {
			ShoppingCart cart = ShoppingCart.startShopping(customerId);
			cart.addItem(productA, new Quantity(1));

			Product unavailableProduct = Product.builder()
					.id(productA.id())
					.productName(productA.productName())
					.price(productA.price())
					.inStock(false)
					.build();
			cart.refreshItem(unavailableProduct);

			try {
				sut.checkout(cart, billing, shipping, paymentMethod);
			} catch (ShoppingCartCantProceedToCheckoutException ignored) {
			}

			assertThat(cart.isEmpty()).isFalse();
			assertThat(cart.items()).isNotEmpty();
		}
	}

	@Nested
	@DisplayName("checkout com carrinho vazio")
	class EmptyCart {

		@Test
		@DisplayName("deve lançar exceção quando carrinho está vazio")
		void shouldThrowException() {
			ShoppingCart cart = ShoppingCart.startShopping(customerId);

			assertThatThrownBy(() -> sut.checkout(cart, billing, shipping, paymentMethod))
					.isInstanceOf(ShoppingCartCantProceedToCheckoutException.class);
		}
	}
}
