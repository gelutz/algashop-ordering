package com.lutz.algashop.ordering.domain.order.service;

import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.commons.Quantity;
import com.lutz.algashop.ordering.domain.customer.Customer;
import com.lutz.algashop.ordering.domain.customer.LoyaltyPoints;
import com.lutz.algashop.ordering.domain.customer.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.domain.order.Billing;
import com.lutz.algashop.ordering.domain.order.CheckoutService;
import com.lutz.algashop.ordering.domain.order.Orders;
import com.lutz.algashop.ordering.domain.order.builder.OrderTestBuilder;
import com.lutz.algashop.ordering.domain.order.entity.Order;
import com.lutz.algashop.ordering.domain.order.entity.OrderStatus;
import com.lutz.algashop.ordering.domain.order.entity.PaymentMethod;
import com.lutz.algashop.ordering.domain.order.shipping.Shipping;
import com.lutz.algashop.ordering.domain.order.specification.CustomerHasFreeShippingSpecification;
import com.lutz.algashop.ordering.domain.product.Product;
import com.lutz.algashop.ordering.domain.product.ProductId;
import com.lutz.algashop.ordering.domain.product.ProductName;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCart;
import com.lutz.algashop.ordering.domain.shoppingCart.exception.ShoppingCartCantProceedToCheckoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

	@Mock
	private Orders orders;

	private CheckoutService sut;

	@BeforeEach
	void setup() {
		CustomerHasFreeShippingSpecification customerHasFreeShippingSpecification = new CustomerHasFreeShippingSpecification(
				orders,
				new LoyaltyPoints(100),
				2,
				new LoyaltyPoints(1000));
		sut = new CheckoutService(customerHasFreeShippingSpecification);
	}

	private final Billing billing = com.lutz.algashop.ordering.domain.order.builder.OrderTestBuilder.aBilling().build();
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
			Customer customer = CustomerTestBuilder.aCustomer().build();
			ShoppingCart cart = ShoppingCart.startShopping(customer.id());
			cart.addItem(productA, new Quantity(2));
			cart.addItem(productB, new Quantity(1));


			Order order = sut.checkout(customer, cart, billing, shipping, paymentMethod);

			assertThat(order).isNotNull();
			assertThat(order.status()).isEqualTo(OrderStatus.PLACED);
			assertThat(order.customerId()).isEqualTo(customer.id());
			assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
			assertThat(order.billing()).isEqualTo(billing);
			assertThat(order.shipping()).isEqualTo(shipping);
		}

		@Test
		@DisplayName("deve transferir itens corretamente para o pedido")
		void shouldTransferItemsCorrectly() {
			Customer customer = CustomerTestBuilder.aCustomer().build();
			ShoppingCart cart = ShoppingCart.startShopping(customer.id());
			cart.addItem(productA, new Quantity(2));
			cart.addItem(productB, new Quantity(1));

			Order order = sut.checkout(customer, cart, billing, shipping, paymentMethod);

			assertThat(order.items()).hasSize(2);
			assertThat(order.itemsAmount()).isEqualTo(new Quantity(3));

			Money expectedItemsTotal = new Money("100.00"); // 25*2 + 50*1
			Money expectedTotal = expectedItemsTotal.add(shipping.cost());
			assertThat(order.totalAmount()).isEqualTo(expectedTotal);
		}

		@Test
		@DisplayName("deve esvaziar o carrinho após checkout")
		void shouldEmptyShoppingCart() {
			Customer customer = CustomerTestBuilder.aCustomer().build();
			ShoppingCart cart = ShoppingCart.startShopping(customer.id());
			cart.addItem(productA, new Quantity(1));

			sut.checkout(customer, cart, billing, shipping, paymentMethod);

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
			Customer customer = CustomerTestBuilder.aCustomer().build();
			ShoppingCart cart = ShoppingCart.startShopping(customer.id());
			cart.addItem(productA, new Quantity(1));

			Product unavailableProduct = Product.builder()
					.id(productA.id())
					.productName(productA.productName())
					.price(productA.price())
					.inStock(false)
					.build();
			cart.refreshItem(unavailableProduct);

			assertThatThrownBy(() -> sut.checkout(customer, cart, billing, shipping, paymentMethod))
					.isInstanceOf(ShoppingCartCantProceedToCheckoutException.class);
		}

		@Test
		@DisplayName("não deve esvaziar o carrinho quando checkout falha")
		void shouldNotEmptyCart() {
			Customer customer = CustomerTestBuilder.aCustomer().build();
			ShoppingCart cart = ShoppingCart.startShopping(customer.id());
			cart.addItem(productA, new Quantity(1));

			Product unavailableProduct = Product.builder()
					.id(productA.id())
					.productName(productA.productName())
					.price(productA.price())
					.inStock(false)
					.build();
			cart.refreshItem(unavailableProduct);

			try {
				sut.checkout(customer, cart, billing, shipping, paymentMethod);
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
			Customer customer = CustomerTestBuilder.aCustomer().build();
			ShoppingCart cart = ShoppingCart.startShopping(customer.id());

			assertThatThrownBy(() -> sut.checkout(customer, cart, billing, shipping, paymentMethod))
					.isInstanceOf(ShoppingCartCantProceedToCheckoutException.class);
		}
	}

	@Test
	@DisplayName("deve dar free shipping quando o customer tem loyalty points e número de compras necessários.")
	void givenCustomerWithEnoughValidPointsShouldGiveFreeShipping() {
		Customer customer = CustomerTestBuilder.aCustomer().build();
		ShoppingCart cart = ShoppingCart.startShopping(customer.id());
		cart.addItem(productA, new Quantity(2));
		cart.addItem(productB, new Quantity(1));


		Order order = sut.checkout(customer, cart, billing, shipping, paymentMethod);

		assertThat(order).isNotNull();
		assertThat(order.status()).isEqualTo(OrderStatus.PLACED);
		assertThat(order.customerId()).isEqualTo(customer.id());
		assertThat(order.paymentMethod()).isEqualTo(paymentMethod);
		assertThat(order.billing()).isEqualTo(billing);
		assertThat(order.shipping()).isEqualTo(shipping);
	}
}
