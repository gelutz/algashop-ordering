package com.lutz.algashop.ordering.presentation.order;

import com.lutz.algashop.ordering.domain.order.OrderId;
import com.lutz.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityRepository;
import com.lutz.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityTestBuilder;
import com.lutz.algashop.ordering.infrastructure.persistence.order.OrderPersistenceEntityRepository;
import com.lutz.algashop.ordering.utils.AlgaShopResourceUtils;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerIT {

	@Autowired
	private CustomerPersistenceEntityRepository customerRepository;

	@Autowired
	private OrderPersistenceEntityRepository orderRepository;

	@LocalServerPort
	private int port;

	private static boolean databaseInitialized;
	private static final UUID validCustomerId = UUID.fromString("6e148bd5-47f6-4022-b9da-07cfaa294f7a");
	@BeforeEach
	public void setup() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;


		RestAssured.config()
		           .jsonConfig(JsonConfig.jsonConfig()
		                                 .numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL)
		           );
		initDatabase();
	}

	private void initDatabase() {
		if (databaseInitialized) return;

		customerRepository.saveAndFlush(
				CustomerPersistenceEntityTestBuilder
						.existing()
						.id(validCustomerId)
						.build()
		);

		databaseInitialized = true;
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
}
