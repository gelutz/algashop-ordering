package com.lutz.algashop.ordering.contract.base;

import com.lutz.algashop.ordering.application.shoppingcart.management.ShoppingCartItemInput;
import com.lutz.algashop.ordering.application.shoppingcart.management.ShoppingCartManagementApplicationService;
import com.lutz.algashop.ordering.application.shoppingcart.query.ShoppingCartOutput;
import com.lutz.algashop.ordering.application.shoppingcart.query.ShoppingCartOutputTestDataBuilder;
import com.lutz.algashop.ordering.application.shoppingcart.query.ShoppingCartQueryService;
import com.lutz.algashop.ordering.domain.shoppingCart.entity.ShoppingCartId;
import com.lutz.algashop.ordering.domain.shoppingCart.exception.ShoppingCartNotFoundException;
import com.lutz.algashop.ordering.presentation.shoppingCart.ShoppingCartController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@WebMvcTest(controllers = ShoppingCartController.class)
public class ShoppingcartBase {

	@Autowired
	private WebApplicationContext context;

	@MockitoBean
	private ShoppingCartQueryService shoppingCartQueryService;

	@MockitoBean
	private ShoppingCartManagementApplicationService shoppingCartManagementApplicationService;

	public static final UUID validShoppingCartId = UUID.fromString("b551a5cf-7462-4751-bdb5-d1961359a4e2");

	public static final UUID notFoundShoppingCartId = UUID.fromString("03ff97bf-c376-41c6-85ed-79b4ffd86e2c");

	public static final UUID validCustomerId = UUID.fromString("2c65f7f8-baf8-4b8f-9c1e-5210bf4a2c65");

	public static final UUID validItemId = UUID.fromString("39c36d6e-fc42-4e6a-9a49-a2a4900e4470");

	@BeforeEach
	void setUp() {
		RestAssuredMockMvc.mockMvc(
				MockMvcBuilders.webAppContextSetup(context)
				               .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
				               .build()
		);

		RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

		ShoppingCartOutput existingCart = ShoppingCartOutputTestDataBuilder.existing(validShoppingCartId, validCustomerId);

		Mockito.when(shoppingCartQueryService.findById(validShoppingCartId))
		       .thenReturn(existingCart);

		Mockito.when(shoppingCartQueryService.findById(notFoundShoppingCartId))
		       .thenThrow(new ShoppingCartNotFoundException(new ShoppingCartId(notFoundShoppingCartId)));

		Mockito.when(shoppingCartManagementApplicationService.createNew(Mockito.any(UUID.class)))
		       .thenReturn(validShoppingCartId);

		Mockito.doNothing().when(shoppingCartManagementApplicationService)
		       .addItem(Mockito.any(ShoppingCartItemInput.class));

		Mockito.doNothing().when(shoppingCartManagementApplicationService)
		       .delete(validShoppingCartId);

		Mockito.doNothing().when(shoppingCartManagementApplicationService)
		       .empty(validShoppingCartId);

		Mockito.doNothing().when(shoppingCartManagementApplicationService)
		       .removeItem(validShoppingCartId, validItemId);
	}
}
