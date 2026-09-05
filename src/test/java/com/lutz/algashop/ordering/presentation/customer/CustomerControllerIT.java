package com.lutz.algashop.ordering.presentation.customer;

import static com.lutz.algashop.ordering.application.customer.management.builder.CustomerInputTestBuilder.aCustomerInput;
import static org.assertj.core.api.Assertions.assertThat;

import com.lutz.algashop.ordering.application.customer.management.CustomerInput;
import com.lutz.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntity;
import com.lutz.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityRepository;
import com.lutz.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityTestBuilder;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CustomerControllerIT {

	@Autowired
	private CustomerPersistenceEntityRepository customerRepository;

	@LocalServerPort
	private int localServerPort;

	@BeforeEach
	public void setup() {
		RestAssured.port = localServerPort;
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

		customerRepository.deleteAll();
	}

	@Test
	public void shouldCreateCustomer() {
		CustomerInput input = aCustomerInput()
				.withEmail(UUID.randomUUID() + "@email.com")
				.build();

		String extractedCustomerId = RestAssured
				.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(input)
				.when()
				.post("/api/v1/customers")
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.statusCode(HttpStatus.CREATED.value())
				.header("Location", Matchers.containsString("/api/v1/customers/"))
				.body("id", Matchers.not(Matchers.emptyString()))
				.extract().jsonPath().getString("id");

		CustomerPersistenceEntity persisted = customerRepository
				.findById(UUID.fromString(extractedCustomerId))
				.orElseThrow();

		assertThat(persisted.getFirstName()).isEqualTo(input.getFirstName());
		assertThat(persisted.getLastName()).isEqualTo(input.getLastName());
		assertThat(persisted.getEmail()).isEqualTo(input.getEmail());
		assertThat(persisted.getDocument()).isEqualTo(input.getDocument());
		assertThat(persisted.getArchived()).isFalse();
	}

	@Test
	public void shouldNotCreateCustomerWhenInputIsInvalid() {
		CustomerInput input = aCustomerInput()
				.withFirstName(null)
				.withEmail("not-an-email")
				.withBirthdate(LocalDate.now().plusYears(1))
				.build();

		RestAssured
				.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(input)
				.when()
				.post("/api/v1/customers")
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body(
						"fields", Matchers.notNullValue(),
						"fields.firstName", Matchers.notNullValue(),
						"fields.email", Matchers.notNullValue(),
						"fields.birthdate", Matchers.notNullValue()
				);

		assertThat(customerRepository.count()).isZero();
	}

	@Test
	public void shouldArchiveCustomer() {
		UUID customerId = UUID.randomUUID();

		customerRepository.saveAndFlush(
				CustomerPersistenceEntityTestBuilder
						.existing()
						.id(customerId)
						.build()
		);

		RestAssured
				.given()
				.when()
				.delete("/api/v1/customers/{customerId}", customerId)
				.then()
				.assertThat()
				.statusCode(HttpStatus.NO_CONTENT.value());

		assertThat(customerRepository.existsById(customerId)).isTrue();

		CustomerPersistenceEntity archived = customerRepository.findById(customerId).orElseThrow();

		assertThat(archived.getArchived()).isTrue();
		assertThat(archived.getArchivedAt()).isNotNull();
		assertThat(archived.getFirstName()).isEqualTo("Archived");
		assertThat(archived.getPhone()).isEqualTo("0");
		assertThat(archived.getDocument()).isEqualTo("0");
		assertThat(archived.getBirthdate()).isNull();
		assertThat(archived.getPromotionNotificationAllowed()).isFalse();
		assertThat(archived.getEmail()).endsWith("@archived.com");
		assertThat(archived.getAddress().getStreet()).isEqualTo("anon");
	}

	@Test
	public void shouldReturnNotFoundWhenArchivingNonExistingCustomer() {
		UUID nonExistingCustomerId = UUID.randomUUID();

		RestAssured
				.given()
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.delete("/api/v1/customers/{customerId}", nonExistingCustomerId)
				.then()
				.assertThat()
				.contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
				.statusCode(HttpStatus.NOT_FOUND.value())
				.body("title", Matchers.is("Not found"));
	}
}
