package com.lutz.algashop.ordering.presentation.shoppingCart;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.lutz.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityRepository;
import com.lutz.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityTestBuilder;
import com.lutz.algashop.ordering.infrastructure.persistence.shoppingCart.ShoppingCartPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.shoppingCart.ShoppingCartPersistenceEntityRepository;
import com.lutz.algashop.ordering.utils.AlgaShopResourceUtils;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.JsonPath;
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
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ShoppingCartControllerIT {

	@Autowired
	private CustomerPersistenceEntityRepository customerRepository;

	@Autowired
	private ShoppingCartPersistenceEntityRepository shoppingCartRepository;

	@LocalServerPort
	private int localServerPort;

	private static final UUID validCustomerId = UUID.fromString("6e148bd5-47f6-4022-b9da-07cfaa294f7a");

	// O item do carrinho guarda o id vindo do CORPO do stub (product.id()), que e diferente
	// do id usado na URL da requisicao (28fcd9fb-...).
	private static final UUID stubProductId = UUID.fromString("2ce650ed-1a96-421f-bff0-4437a54d2b58");

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
		// Defensivo: com create-drop + @DirtiesContext o banco ja deveria nascer limpo, mas isso
		// deixa explicita a invariante "um carrinho por cliente". Carrinhos antes de clientes (FK).
		shoppingCartRepository.deleteAll();
		customerRepository.deleteAll();

		customerRepository.saveAndFlush(
				CustomerPersistenceEntityTestBuilder
						.existing()
						.id(validCustomerId)
						.build()
		);
	}

	@Test
	public void shouldCreateShoppingCart() {
		String createInput = AlgaShopResourceUtils.readContent("json/createShoppingCartInput.json");
		String extractedShoppingCartId = RestAssured
				.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(createInput)
				.when()
				.post("/api/v1/shopping-carts")
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.statusCode(HttpStatus.CREATED.value())
				.body(
						"id", Matchers.not(Matchers.emptyString()),
						"customerId", Matchers.is(validCustomerId.toString())
				)
				.extract().jsonPath().getString("id");

		Assertions.assertNotNull(extractedShoppingCartId);

		boolean shoppingCartWasCreated = shoppingCartRepository
				.existsById(UUID.fromString(extractedShoppingCartId));
		Assertions.assertTrue(shoppingCartWasCreated);
	}

	@Test
	public void shouldNotCreateShoppingCartWhenCustomerIdIsNull() {
		String createInput = AlgaShopResourceUtils
				.readContent("json/createShoppingCartWithInvalidCustomerIdInput.json");
		RestAssured
				.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(createInput)
				.when()
				.post("/api/v1/shopping-carts")
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("fields.customerId", Matchers.not(Matchers.emptyOrNullString()));
	}

	@Test
	public void shouldNotCreateShoppingCartWhenCustomerAlreadyHasOne() {
		createShoppingCart();

		String createInput = AlgaShopResourceUtils.readContent("json/createShoppingCartInput.json");
		RestAssured
				.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(createInput)
				.when()
				.post("/api/v1/shopping-carts")
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
				.statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
	}

	@Test
	public void shouldAddItemToShoppingCart() {
		UUID shoppingCartId = createShoppingCart();

		String addItemInput = AlgaShopResourceUtils.readContent("json/addShoppingCartItemInput.json");
		RestAssured
				.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(addItemInput)
				.when()
				.post("/api/v1/shopping-carts/{shoppingCartId}/items", shoppingCartId)
				.then()
				.assertThat()
				.statusCode(HttpStatus.NO_CONTENT.value());

		JsonPath shoppingCart = RestAssured
				.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.get("/api/v1/shopping-carts/{shoppingCartId}", shoppingCartId)
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.statusCode(HttpStatus.OK.value())
				.body(
						"id", Matchers.is(shoppingCartId.toString()),
						"customerId", Matchers.is(validCustomerId.toString()),
						"totalItems", Matchers.is(2),
						"items", Matchers.hasSize(1),
						"items[0].productId", Matchers.is(stubProductId.toString()),
						"items[0].name", Matchers.is("Notebook Linux"),
						"items[0].quantity", Matchers.is(2),
						"items[0].available", Matchers.is(true)
				)
				.extract().jsonPath()
				.using(JsonPathConfig.jsonPathConfig()
				                     .numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL));

		Assertions.assertEquals(0,
				shoppingCart.getObject("totalAmount", BigDecimal.class)
				            .compareTo(new BigDecimal("2000.00")));
		Assertions.assertEquals(0,
				shoppingCart.getObject("items[0].price", BigDecimal.class)
				            .compareTo(new BigDecimal("1000.00")));
		Assertions.assertEquals(0,
				shoppingCart.getObject("items[0].totalAmount", BigDecimal.class)
				            .compareTo(new BigDecimal("2000.00")));

		ShoppingCartPersistenceEntity persistedShoppingCart = shoppingCartRepository
				.findById(shoppingCartId)
				.orElseThrow();

		// a colecao de itens e lazy e o teste roda fora de transacao, entao contamos
		// as linhas direto no banco em vez de navegar a associacao
		Assertions.assertEquals(1,
			shoppingCartRepository.countItemsByShoppingCartId(shoppingCartId));
		Assertions.assertEquals(2, persistedShoppingCart.getTotalItems());
		Assertions.assertEquals(0,
				persistedShoppingCart.getTotalAmount().compareTo(new BigDecimal("2000.00")));
	}

	@Test
	public void shouldNotAddItemWhenShoppingCartNotFound() {
		UUID notFoundShoppingCartId = UUID.randomUUID();

		// Produto valido de proposito: prova que o 404 vem do carrinho ausente, nao do produto.
		String addItemInput = AlgaShopResourceUtils.readContent("json/addShoppingCartItemInput.json");
		RestAssured
				.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(addItemInput)
				.when()
				.post("/api/v1/shopping-carts/{shoppingCartId}/items", notFoundShoppingCartId)
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
				.statusCode(HttpStatus.NOT_FOUND.value());
	}

	@Test
	public void shouldNotAddItemWhenProductNotFound() {
		UUID shoppingCartId = createShoppingCart();

		String addItemInput = AlgaShopResourceUtils
				.readContent("json/addShoppingCartItemWithInvalidProductInput.json");
		RestAssured
				.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(addItemInput)
				.when()
				.post("/api/v1/shopping-carts/{shoppingCartId}/items", shoppingCartId)
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
				.statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
	}

	private UUID createShoppingCart() {
		String createInput = AlgaShopResourceUtils.readContent("json/createShoppingCartInput.json");
		String shoppingCartId = RestAssured
				.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(createInput)
				.when()
				.post("/api/v1/shopping-carts")
				.then()
				.assertThat()
				.statusCode(HttpStatus.CREATED.value())
				.extract().jsonPath().getString("id");

		return UUID.fromString(shoppingCartId);
	}
}
