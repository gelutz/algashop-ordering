package com.lutz.algashop.ordering.application.checkout;

import com.lutz.algashop.ordering.application.checkout.builder.BuyNowInputTestBuilder;
import com.lutz.algashop.ordering.domain.commons.Money;
import com.lutz.algashop.ordering.domain.customer.Customers;
import com.lutz.algashop.ordering.domain.customer.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.domain.order.OrderId;
import com.lutz.algashop.ordering.domain.order.Orders;
import com.lutz.algashop.ordering.domain.order.shipping.ShippingCostService;
import com.lutz.algashop.ordering.domain.product.Product;
import com.lutz.algashop.ordering.domain.product.ProductCatalogService;
import com.lutz.algashop.ordering.domain.product.builder.ProductTestBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@Transactional
public class BuyNowApplicationServiceIT {

	@Autowired
	private BuyNowApplicationService sut;

	@Autowired
	private Orders orders;

	@Autowired
	private Customers customers;

	@MockitoBean
	private ProductCatalogService productCatalogService;

	@MockitoBean
	private ShippingCostService shippingCostService;
	@Autowired
	private BuyNowApplicationService buyNowApplicationService;

	@BeforeEach
	void setup() {
		if (!customers.exists(CustomerTestBuilder.DEFAULT_CUSTOMER_ID)) {
			customers.add(CustomerTestBuilder.aCustomer().build());
		}
	}

	@Test
	public void shouldBuyNow() {
		Product product = ProductTestBuilder.aProduct().build();
		Mockito.when(productCatalogService.ofId(product.id())).thenReturn(Optional.of(product));

		Mockito.when(shippingCostService.calculate(Mockito.any(ShippingCostService.CalculationRequest.class)))
				.thenReturn(new ShippingCostService.CalculationResult(
						new Money("10"),
						LocalDate.now().plusDays(3)
				));

		BuyNowInput input = BuyNowInputTestBuilder.aBuyNowInput().build();

		String orderId = buyNowApplicationService.buyNow(input);
		Assertions.assertNotNull(orderId);
		Assertions.assertTrue(orders.exists(new OrderId(orderId)));
	}
}
