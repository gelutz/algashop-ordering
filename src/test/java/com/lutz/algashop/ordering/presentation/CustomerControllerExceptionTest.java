package com.lutz.algashop.ordering.presentation;

import com.lutz.algashop.ordering.application.customer.management.CustomerInput;
import com.lutz.algashop.ordering.application.customer.management.CustomerUpdateInput;
import com.lutz.algashop.ordering.domain.DomainException;
import com.lutz.algashop.ordering.domain.customer.CustomerArchivedException;
import com.lutz.algashop.ordering.domain.customer.CustomerEmailIsInUseException;
import com.lutz.algashop.ordering.domain.customer.CustomerNotFoundException;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.UUID;

class CustomerControllerExceptionTest extends AbstractCustomerControllerTest {

	@Test
	public void findByIdError404Contract() {
		UUID invalidCustomerId = UUID.randomUUID();

		Mockito.when(customerQueryServiceMock.findById(invalidCustomerId))
		       .thenThrow(CustomerNotFoundException.class);

		RestAssuredMockMvc
				.given()
				.accept(MediaType.APPLICATION_JSON)
				.when()
				.get("/api/v1/customers/{customerId}", invalidCustomerId)
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
				.statusCode(HttpStatus.NOT_FOUND.value())
				.body(
						"status", Matchers.is(HttpStatus.NOT_FOUND.value()),
						"type", Matchers.is("/errors/not-found"),
						"title", Matchers.notNullValue(),
						"instance", Matchers.notNullValue()
				);

	}

	@Test
	public void createCustomerError409Contract() {
		Mockito.when(customerManagementApplicationServiceMock.create(Mockito.any(CustomerInput.class)))
		       .thenThrow(CustomerEmailIsInUseException.class);

		String jsonInput = """
        {
          "firstName": "John",
          "lastName": "Doe",
          "email": "johndoe@email.com",
          "document": "12345",
          "phone": "1191234564",
          "birthdate": "1991-07-05",
          "promotionNotificationsAllowed": false,
          "address": {
            "street": "Bourbon Street",
            "number": "2000",
            "complement": "apt 122",
            "neighborhood": "North Ville",
            "city": "Yostfort",
            "state": "South Carolina",
            "zipCode": "12321"
          }
        }
        """;

		RestAssuredMockMvc
				.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.body(jsonInput)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/api/v1/customers")
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
				.statusCode(HttpStatus.CONFLICT.value())
				.body(
						"status", Matchers.is(HttpStatus.CONFLICT.value()),
						"type", Matchers.is("/errors/conflict"),
						"title", Matchers.notNullValue(),
						"instance", Matchers.notNullValue()
				);
	}

	@Test
	public void createCustomerError422Contract() {
		Mockito.when(customerManagementApplicationServiceMock.create(Mockito.any(CustomerInput.class)))
		       .thenThrow(DomainException.class);

		String jsonInput = """
        {
          "firstName": "John",
          "lastName": "Doe",
          "email": "johndoe@email.com",
          "document": "12345",
          "phone": "1191234564",
          "birthdate": "1991-07-05",
          "promotionNotificationsAllowed": false,
          "address": {
            "street": "Bourbon Street",
            "number": "2000",
            "complement": "apt 122",
            "neighborhood": "North Ville",
            "city": "Yostfort",
            "state": "South Carolina",
            "zipCode": "12321"
          }
        }
        """;

		RestAssuredMockMvc
				.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.body(jsonInput)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/api/v1/customers")
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
				.statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
				.body(
						"status", Matchers.is(HttpStatus.UNPROCESSABLE_ENTITY.value()),
						"type", Matchers.is("/errors/unprocessable-entity"),
						"title", Matchers.notNullValue(),
						"instance", Matchers.notNullValue()
				);
	}

	@Test
	public void createCustomerError500Contract() {
		Mockito.when(customerManagementApplicationServiceMock.create(Mockito.any(CustomerInput.class)))
		       .thenThrow(RuntimeException.class);

		String jsonInput = """
        {
          "firstName": "John",
          "lastName": "Doe",
          "email": "johndoe@email.com",
          "document": "12345",
          "phone": "1191234564",
          "birthdate": "1991-07-05",
          "promotionNotificationsAllowed": false,
          "address": {
            "street": "Bourbon Street",
            "number": "2000",
            "complement": "apt 122",
            "neighborhood": "North Ville",
            "city": "Yostfort",
            "state": "South Carolina",
            "zipCode": "12321"
          }
        }
        """;

		RestAssuredMockMvc
				.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.body(jsonInput)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/api/v1/customers")
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
				.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.body(
						"status", Matchers.is(HttpStatus.INTERNAL_SERVER_ERROR.value()),
						"type", Matchers.is("/errors/internal"),
						"title", Matchers.notNullValue(),
						"instance", Matchers.notNullValue()
				);
	}

	private String validUpdateJsonInput() {
		return """
				{
				  "firstName": "John",
				  "lastName": "Doe",
				  "phone": "1191234564",
				  "promotionNotificationsAllowed": false,
				  "address": {
				    "street": "Bourbon Street",
				    "number": "2000",
				    "complement": "apt 122",
				    "neighborhood": "North Ville",
				    "city": "Yostfort",
				    "state": "South Carolina",
				    "zipCode": "12321"
				  }
				}
				""";
	}

	@Test
	public void updateCustomerError400Contract() {
		UUID customerId = UUID.randomUUID();

		String jsonInput = """
				{
				  "firstName": "",
				  "lastName": "Doe",
				  "phone": "1191234564",
				  "promotionNotificationsAllowed": false,
				  "address": {
				    "street": "Bourbon Street",
				    "number": "2000",
				    "complement": "apt 122",
				    "neighborhood": "North Ville",
				    "city": "Yostfort",
				    "state": "South Carolina",
				    "zipCode": "12321"
				  }
				}
				""";

		RestAssuredMockMvc
				.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.body(jsonInput)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.put("/api/v1/customers/{customerId}", customerId)
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body(
						"status", Matchers.is(HttpStatus.BAD_REQUEST.value()),
						"type", Matchers.is("/errors/invalid-fields"),
						"title", Matchers.notNullValue(),
						"instance", Matchers.notNullValue(),
						"fields.firstName", Matchers.notNullValue()
				);
	}

	@Test
	public void updateCustomerError404Contract() {
		UUID customerId = UUID.randomUUID();

		Mockito.doThrow(CustomerNotFoundException.class)
		       .when(customerManagementApplicationServiceMock)
		       .update(Mockito.eq(customerId), Mockito.any(CustomerUpdateInput.class));

		RestAssuredMockMvc
				.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.body(validUpdateJsonInput())
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.put("/api/v1/customers/{customerId}", customerId)
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
				.statusCode(HttpStatus.NOT_FOUND.value())
				.body(
						"status", Matchers.is(HttpStatus.NOT_FOUND.value()),
						"type", Matchers.is("/errors/not-found"),
						"title", Matchers.notNullValue(),
						"instance", Matchers.notNullValue()
				);
	}

	@Test
	public void updateCustomerError409Contract() {
		UUID customerId = UUID.randomUUID();

		Mockito.doThrow(CustomerEmailIsInUseException.class)
		       .when(customerManagementApplicationServiceMock)
		       .update(Mockito.eq(customerId), Mockito.any(CustomerUpdateInput.class));

		RestAssuredMockMvc
				.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.body(validUpdateJsonInput())
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.put("/api/v1/customers/{customerId}", customerId)
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
				.statusCode(HttpStatus.CONFLICT.value())
				.body(
						"status", Matchers.is(HttpStatus.CONFLICT.value()),
						"type", Matchers.is("/errors/conflict"),
						"title", Matchers.notNullValue(),
						"instance", Matchers.notNullValue()
				);
	}

	@Test
	public void updateCustomerError422Contract() {
		UUID customerId = UUID.randomUUID();

		Mockito.doThrow(CustomerArchivedException.class)
		       .when(customerManagementApplicationServiceMock)
		       .update(Mockito.eq(customerId), Mockito.any(CustomerUpdateInput.class));

		RestAssuredMockMvc
				.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.body(validUpdateJsonInput())
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.put("/api/v1/customers/{customerId}", customerId)
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
				.statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
				.body(
						"status", Matchers.is(HttpStatus.UNPROCESSABLE_ENTITY.value()),
						"type", Matchers.is("/errors/unprocessable-entity"),
						"title", Matchers.notNullValue(),
						"instance", Matchers.notNullValue()
				);
	}

	@Test
	public void updateCustomerError500Contract() {
		UUID customerId = UUID.randomUUID();

		Mockito.doThrow(RuntimeException.class)
		       .when(customerManagementApplicationServiceMock)
		       .update(Mockito.eq(customerId), Mockito.any(CustomerUpdateInput.class));

		RestAssuredMockMvc
				.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.body(validUpdateJsonInput())
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.put("/api/v1/customers/{customerId}", customerId)
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
				.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.body(
						"status", Matchers.is(HttpStatus.INTERNAL_SERVER_ERROR.value()),
						"type", Matchers.is("/errors/internal"),
						"title", Matchers.notNullValue(),
						"instance", Matchers.notNullValue()
				);
	}

	@Test
	public void deleteCustomerError404Contract() {
		UUID customerId = UUID.randomUUID();

		Mockito.doThrow(CustomerNotFoundException.class)
		       .when(customerManagementApplicationServiceMock)
		       .archive(customerId);

		RestAssuredMockMvc
				.given()
				.when()
				.delete("/api/v1/customers/{customerId}", customerId)
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
				.statusCode(HttpStatus.NOT_FOUND.value())
				.body(
						"status", Matchers.is(HttpStatus.NOT_FOUND.value()),
						"type", Matchers.is("/errors/not-found"),
						"title", Matchers.notNullValue(),
						"instance", Matchers.notNullValue()
				);
	}

	@Test
	public void deleteCustomerError422Contract() {
		UUID customerId = UUID.randomUUID();

		Mockito.doThrow(CustomerArchivedException.class)
		       .when(customerManagementApplicationServiceMock)
		       .archive(customerId);

		RestAssuredMockMvc
				.given()
				.when()
				.delete("/api/v1/customers/{customerId}", customerId)
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
				.statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
				.body(
						"status", Matchers.is(HttpStatus.UNPROCESSABLE_ENTITY.value()),
						"type", Matchers.is("/errors/unprocessable-entity"),
						"title", Matchers.notNullValue(),
						"instance", Matchers.notNullValue()
				);
	}

	@Test
	public void deleteCustomerError500Contract() {
		UUID customerId = UUID.randomUUID();

		Mockito.doThrow(RuntimeException.class)
		       .when(customerManagementApplicationServiceMock)
		       .archive(customerId);

		RestAssuredMockMvc
				.given()
				.when()
				.delete("/api/v1/customers/{customerId}", customerId)
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
				.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.body(
						"status", Matchers.is(HttpStatus.INTERNAL_SERVER_ERROR.value()),
						"type", Matchers.is("/errors/internal"),
						"title", Matchers.notNullValue(),
						"instance", Matchers.notNullValue()
				);
	}
}
