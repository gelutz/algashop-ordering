package com.lutz.algashop.ordering.presentation.order;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.lutz.algashop.ordering.application.checkout.CheckoutInput;
import com.lutz.algashop.ordering.application.checkout.builder.CheckoutInputTestBuilder;
import com.lutz.algashop.ordering.domain.order.OrderId;
import com.lutz.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityRepository;
import com.lutz.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityTestBuilder;
import com.lutz.algashop.ordering.infrastructure.persistence.order.OrderPersistenceEntityRepository;
import com.lutz.algashop.ordering.infrastructure.persistence.shoppingCart.ShoppingCartItemPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.shoppingCart.ShoppingCartPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.shoppingCart.ShoppingCartPersistenceEntityRepository;
import com.lutz.algashop.ordering.infrastructure.persistence.shoppingCart.ShoppingCartPersistenceEntityTestBuilder;
import com.lutz.algashop.ordering.utils.AlgaShopResourceUtils;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderControllerIT {

	@Autowired
	private CustomerPersistenceEntityRepository customerRepository;

	@Autowired
	private OrderPersistenceEntityRepository orderRepository;

	@Autowired
	private ShoppingCartPersistenceEntityRepository shoppingCartRepository;

	private CustomerPersistenceEntity savedCustomer;

	@LocalServerPort
	private int localServerPort;

	private static final UUID validCustomerId = UUID.fromString("6e148bd5-47f6-4022-b9da-07cfaa294f7a");

	@Value("${algashop.integrations.product-catalog.wiremock.port:8187}")
	private int productCatalogWireMockPort;
	@Value("${algashop.integrations.shipping.wiremock.port:8087}")
	private int rapidexWireMockPort;

	private WireMockServer productCatalogWireMockServer;
	private WireMockServer rapidexWireMockServer;


	@BeforeEach
	public void setup() {
		RestAssured.port = localServerPort;
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

		RestAssured.config()
		           .jsonConfig(JsonConfig.jsonConfig()
		                                 .numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL)
		           );

		productCatalogWireMockServer = new WireMockServer(
				WireMockConfiguration.options()
				                     .port(productCatalogWireMockPort)
						.usingFilesUnderClasspath("wiremock/product-catalog")
						.extensions(new ResponseTemplateTransformer(true))
		);

		rapidexWireMockServer = new WireMockServer(
				WireMockConfiguration.options()
				                     .port(rapidexWireMockPort)
				                     .usingFilesUnderClasspath("wiremock/rapidex")
				                     .extensions(new ResponseTemplateTransformer(true))
		);

		productCatalogWireMockServer.start();
		rapidexWireMockServer.start();

		initDatabase();
	}

	@AfterEach
	public void destroy() {
		productCatalogWireMockServer.stop();
		rapidexWireMockServer.stop();
	}
	private void initDatabase() {
		savedCustomer = customerRepository.saveAndFlush(
				CustomerPersistenceEntityTestBuilder
						.existing()
						.id(validCustomerId)
						.build()
		);
	}

	private ShoppingCartPersistenceEntity givenAShoppingCart() {
		return shoppingCartRepository.saveAndFlush(
				ShoppingCartPersistenceEntityTestBuilder
						.existing()
						.customer(savedCustomer)
						.build()
		);
	}

	private ShoppingCartPersistenceEntity givenAnEmptyShoppingCart() {
		return shoppingCartRepository.saveAndFlush(
				ShoppingCartPersistenceEntityTestBuilder
						.existing()
						.customer(savedCustomer)
						.items(Set.of())
						.totalItems(0)
						.totalAmount(BigDecimal.ZERO)
						.build()
		);
	}

	@Test
	public void shouldCreateOrderUsingProduct() {
		String createInput = AlgaShopResourceUtils.readContent("json/createOrderWithProductInput.json");
		String extractedOrderId = RestAssured
				.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType("application/vnd.order-with-product.v1+json")
				.body(createInput)
				.when()
				.post("/api/v1/orders")
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.statusCode(HttpStatus.CREATED.value())
				.body(
						"id", Matchers.not(Matchers.emptyString()),
						"customer.id", Matchers.is(validCustomerId.toString())
				)
				.extract().jsonPath().getString("id");

		boolean orderWasCreated = orderRepository.existsById(new OrderId(extractedOrderId).value().toLong());
		Assertions.assertTrue(orderWasCreated);
	}

	@Test
	public void shouldNotCreateOrderUsingProductWhenCustomerNotFound() {
		String createInput = AlgaShopResourceUtils.readContent("json/createOrderWithInvalidCustomerId.json");
		RestAssured
				.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType("application/vnd.order-with-product.v1+json")
				.body(createInput)
				.when()
				.post("/api/v1/orders")
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
				.statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
	}

	@Test
	public void shouldNotCreateOrderUsingProductWhenProductAPIIsNotAvailable() {
		productCatalogWireMockServer.stop();

		String createInput = AlgaShopResourceUtils.readContent("json/createOrderWithProductInput.json");
		RestAssured
				.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType("application/vnd.order-with-product.v1+json")
				.body(createInput)
				.when()
				.post("/api/v1/orders")
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
				.statusCode(HttpStatus.GATEWAY_TIMEOUT.value());
	}

	@Test
	public void shouldNotCreateOrderUsingProductWhenInvalidProductInput() {
		String createInput = AlgaShopResourceUtils.readContent("json/createOrderWithInvalidProductInput.json");
		RestAssured
				.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType("application/vnd.order-with-product.v1+json")
				.body(createInput)
				.when()
				.post("/api/v1/orders")
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
				.statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
	}

	@Test
	public void shouldCreateOrderUsingShoppingCart() {
		ShoppingCartPersistenceEntity shoppingCart = givenAShoppingCart();

		// o pedido recalcula os totais a partir dos itens, entao somamos as
		// quantidades em vez de confiar no totalItems gravado no carrinho
		int expectedTotalItems = shoppingCart.getItems().stream()
				.mapToInt(ShoppingCartItemPersistenceEntity::getQuantity)
				.sum();

		CheckoutInput createInput = CheckoutInputTestBuilder.aCheckoutInput()
				.shoppingCartId(shoppingCart.getId())
				.build();

		String extractedOrderId = RestAssured
				.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType("application/vnd.order-with-shopping-cart.v1+json")
				.body(createInput)
				.when()
				.post("/api/v1/orders")
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.statusCode(HttpStatus.CREATED.value())
				.header("Location", Matchers.containsString("/api/v1/orders/"))
				.body(
						"id", Matchers.not(Matchers.emptyString()),
						"customer.id", Matchers.is(validCustomerId.toString()),
						"status", Matchers.is("PLACED"),
						"totalItems", Matchers.is(expectedTotalItems),
						"items", Matchers.hasSize(shoppingCart.getItems().size())
				)
				.extract().jsonPath().getString("id");

		boolean orderWasCreated = orderRepository.existsById(new OrderId(extractedOrderId).value().toLong());
		Assertions.assertTrue(orderWasCreated);

		// o checkout esvazia o carrinho; a colecao de itens e lazy e o teste roda
		// fora de transacao, entao conferimos os totais, que empty() zera junto
		ShoppingCartPersistenceEntity updatedShoppingCart =
				shoppingCartRepository.findById(shoppingCart.getId()).orElseThrow();
		Assertions.assertEquals(0, updatedShoppingCart.getTotalItems());
		Assertions.assertEquals(0, updatedShoppingCart.getTotalAmount().compareTo(BigDecimal.ZERO));
	}

	@Test
	public void shouldNotCreateOrderUsingShoppingCartWhenShoppingCartNotFound() {
		CheckoutInput createInput = CheckoutInputTestBuilder.aCheckoutInput()
				.shoppingCartId(UUID.randomUUID())
				.build();

		RestAssured
				.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType("application/vnd.order-with-shopping-cart.v1+json")
				.body(createInput)
				.when()
				.post("/api/v1/orders")
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
				.statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
	}

	@Test
	public void shouldNotCreateOrderUsingShoppingCartWhenShoppingCartIsEmpty() {
		ShoppingCartPersistenceEntity emptyShoppingCart = givenAnEmptyShoppingCart();

		CheckoutInput createInput = CheckoutInputTestBuilder.aCheckoutInput()
				.shoppingCartId(emptyShoppingCart.getId())
				.build();

		RestAssured
				.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType("application/vnd.order-with-shopping-cart.v1+json")
				.body(createInput)
				.when()
				.post("/api/v1/orders")
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
				.statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
	}
}
