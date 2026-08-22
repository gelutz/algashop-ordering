package com.lutz.algashop.ordering.contract.base;


import com.lutz.algashop.ordering.application.checkout.BuyNowApplicationService;
import com.lutz.algashop.ordering.application.checkout.CheckoutApplicationService;
import com.lutz.algashop.ordering.application.order.query.OrderFilter;
import com.lutz.algashop.ordering.application.order.query.OrderQueryService;
import com.lutz.algashop.ordering.domain.order.exception.OrderNotFoundException;
import com.lutz.algashop.ordering.presentation.order.OrderController;
import com.lutz.algashop.ordering.presentation.order.OrderDetailOutputTestBuilder;
import com.lutz.algashop.ordering.presentation.order.OrderSummaryOutputTestBuilder;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;

@WebMvcTest(controllers = OrderController.class)
public class OrderBase {

	@Autowired
	private WebApplicationContext context;

	@MockitoBean
	private OrderQueryService orderQueryService;

	@MockitoBean
	private CheckoutApplicationService checkoutApplicationService;

	@MockitoBean
	private BuyNowApplicationService buyNowApplicationService;

	public static final String validOrderId = "01226N0640J7Q";

	public static final String notFoundOrderId = "01226N0693HDH";

	@BeforeEach
	void setUp() {
		RestAssuredMockMvc.mockMvc(
				MockMvcBuilders.webAppContextSetup(context)
				               .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
				               .build()
		);

		RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

		mockFindById();
		mockFindByIdNotFound();
		mockBuyNow();
		mockCheckout();
		mockFilter();
	}

	private void mockFindById() {
		Mockito.when(orderQueryService.findById(validOrderId))
		       .thenReturn(OrderDetailOutputTestBuilder.placedOrder(validOrderId).build());
	}

	private void mockFindByIdNotFound() {
		Mockito.when(orderQueryService.findById(notFoundOrderId))
		       .thenThrow(new OrderNotFoundException());
	}

	private void mockBuyNow() {
		Mockito.when(buyNowApplicationService.buyNow(Mockito.any()))
		       .thenReturn(validOrderId);
	}

	private void mockCheckout() {
		Mockito.when(checkoutApplicationService.checkout(Mockito.any()))
		       .thenReturn(validOrderId);
	}

	private void mockFilter() {
		Mockito.when(orderQueryService.filter(Mockito.any(OrderFilter.class)))
		       .then((answer) -> {
			       OrderFilter filter = answer.getArgument(0);

			       return new PageImpl<>(
					       List.of(
							       OrderSummaryOutputTestBuilder.placedOrder().build(),
							       OrderSummaryOutputTestBuilder.placedOrderAlt1().build()
					       ),
					       PageRequest.of(filter.getPage(), filter.getSize()),
					       2
			       );
		       });
	}

}
