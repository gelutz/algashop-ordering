package com.lutz.algashop.ordering.domain.entity.shoppingCart;

import com.lutz.algashop.ordering.domain.entity.customer.vo.CustomerId;
import com.lutz.algashop.ordering.domain.entity.order.vo.Money;
import com.lutz.algashop.ordering.domain.entity.order.vo.Quantity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

@SuppressWarnings("DataFlowIssue")
class ShoppingCartTest {
	@DisplayName("ShoppingCart#startShopping and ShoppingCart#existing builders")
	@Nested
	class ShoppingCartBuilderTests {
		@Test
		void startShoppingShouldStartWithDefaultValues() {
			ShoppingCart sut = ShoppingCart.startShopping(new CustomerId());

			Money expectedTotalAmount = Money.ZERO;
			Quantity expectedTotalItems = Quantity.ZERO;

			Assertions.assertEquals(expectedTotalAmount, sut.totalAmount());
			Assertions.assertEquals(expectedTotalItems, sut.totalItems());
			Assertions.assertTrue(sut.createdAt().isBefore(OffsetDateTime.now()));
		}

		@Test
		void existingShouldThrowNullPointerExceptionIfAnyFieldIsNull() {
			ShoppingCartId id = new ShoppingCartId();
			CustomerId customerId = new CustomerId();
			Money totalAmount = new Money("10");
			Quantity totalItems = new Quantity(1);
			OffsetDateTime createdAt = OffsetDateTime.now();

			ShoppingCart.ExistingShoppingCartBuilder sut = ShoppingCart.existing()
			                               .id(id)
			                               .customerId(customerId)
			                               .totalAmount(totalAmount)
			                               .totalItems(totalItems)
			                               .createdAt(createdAt);

			Assertions.assertThrows(NullPointerException.class, () -> sut.id(null).build());
			Assertions.assertThrows(NullPointerException.class, () -> sut.customerId(null).build());
			Assertions.assertThrows(NullPointerException.class, () -> sut.totalAmount(null).build());
			Assertions.assertThrows(NullPointerException.class, () -> sut.totalItems(null).build());
			Assertions.assertThrows(NullPointerException.class, () -> sut.createdAt(null).build());
		}
	}
}