package com.lutz.algashop.ordering.application.customers.management;

import com.lutz.algashop.ordering.application.commons.AddressData;
import com.lutz.algashop.ordering.application.customer.management.CustomerManagementApplicationService;
import com.lutz.algashop.ordering.application.customer.management.CustomerOutput;
import com.lutz.algashop.ordering.application.customer.management.CustomerUpdateInput;
import com.lutz.algashop.ordering.application.customer.notification.CustomerNotificationService;
import com.lutz.algashop.ordering.domain.customer.*;
import com.lutz.algashop.ordering.infrastructure.listener.customer.CustomerEventListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.lutz.algashop.ordering.application.customer.management.builder.CustomerInputTestBuilder.aCustomerInput;
import static com.lutz.algashop.ordering.application.customer.management.builder.CustomerUpdateInputTestBuilder.aCustomerUpdateInput;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class CustomerManagementApplicationServiceIT {

	@Autowired
	private CustomerManagementApplicationService sut;

	@MockitoSpyBean
	private CustomerEventListener customerEventListener;

	@MockitoSpyBean
	private CustomerNotificationService customerNotificationService;

	@Test
	void shouldRegister() {
		var customer = aCustomerInput().build();
		UUID resultingId = sut.create(customer);

		Assertions.assertNotNull(resultingId);

		CustomerOutput resultingOutput = sut.findById(resultingId);

		Assertions.assertEquals(customer.getFirstName(), resultingOutput.getFirstName());
		Assertions.assertEquals(customer.getLastName(), resultingOutput.getLastName());
		Assertions.assertEquals(customer.getEmail(), resultingOutput.getEmail());
		Assertions.assertEquals(customer.getPhone(), resultingOutput.getPhone());
		Assertions.assertEquals(customer.getDocument(), resultingOutput.getDocument());
		Assertions.assertEquals(customer.getBirthdate(), resultingOutput.getBirthdate());

		Mockito.verify(customerEventListener).listen(Mockito.any(CustomerRegisteredEvent.class));

		Mockito.verify(customerNotificationService).notifyNewRegistration(Mockito.any(CustomerNotificationService.NotifyNewRegistrationInput.class));
	}

	@Test
	void shouldUpdateCustomer() {
		UUID customerId = sut.create(aCustomerInput().build());

		CustomerUpdateInput updateInput = aCustomerUpdateInput()
				.withFirstName("Jane")
				.withLastName("Smith")
				.withPhone("999888777")
				.withPromotionNotificationsAllowed(true)
				.withAddress(AddressData.builder()
				                        .street("New street")
				                        .number("200")
				                        .complement("apt 5")
				                        .neighborhood("downtown")
				                        .city("Manhattan")
				                        .state("New York")
				                        .zipCode("456456")
				                        .build())
				.build();

		sut.update(customerId, updateInput);

		CustomerOutput updated = sut.findById(customerId);

		Assertions.assertEquals("Jane", updated.getFirstName());
		Assertions.assertEquals("Smith", updated.getLastName());
		Assertions.assertEquals("999888777", updated.getPhone());
		Assertions.assertEquals(true, updated.getPromotionNotificationAllowed());
		Assertions.assertEquals("New street", updated.getAddress().getStreet());
		Assertions.assertEquals("200", updated.getAddress().getNumber());
		Assertions.assertEquals("apt 5", updated.getAddress().getComplement());
		Assertions.assertEquals("downtown", updated.getAddress().getNeighborhood());
		Assertions.assertEquals("Manhattan", updated.getAddress().getCity());
		Assertions.assertEquals("New York", updated.getAddress().getState());
		Assertions.assertEquals("456456", updated.getAddress().getZipCode());
	}

	@Test
	void shouldThrowWhenUpdatingNonExistentCustomer() {
		CustomerUpdateInput updateInput = aCustomerUpdateInput().build();

		Assertions.assertThrows(CustomerNotFoundException.class,
				() -> sut.update(UUID.randomUUID(), updateInput));
	}

	@Test
	void shouldArchiveCustomer() {
		var input = aCustomerInput().build();
		UUID customerId = sut.create(input);

		sut.archive(customerId);

		CustomerOutput result = sut.findById(customerId);

		Assertions.assertTrue(result.getArchived());
		Assertions.assertNotNull(result.getArchivedAt());
		Assertions.assertEquals("Archived", result.getFirstName());
		Assertions.assertEquals("-", result.getLastName());
		Assertions.assertTrue(result.getEmail().endsWith("@archived.com"));
		Assertions.assertEquals("0", result.getPhone());
		Assertions.assertEquals("0", result.getDocument());
		Assertions.assertNull(result.getBirthdate());
		Assertions.assertFalse(result.getPromotionNotificationAllowed());
		Assertions.assertEquals("000", result.getAddress().getNumber());
		Assertions.assertEquals("anon", result.getAddress().getStreet());
		Assertions.assertEquals("anon", result.getAddress().getCity());
		Assertions.assertEquals("anon", result.getAddress().getState());
	}

	@Test
	void shouldThrowWhenArchivingNonExistentCustomer() {
		Assertions.assertThrows(CustomerNotFoundException.class,
				() -> sut.archive(UUID.randomUUID()));
	}

	@Test
	void shouldThrowWhenArchivingAlreadyArchivedCustomer() {
		UUID customerId = sut.create(aCustomerInput().build());
		sut.archive(customerId);

		Assertions.assertThrows(CustomerArchivedException.class,
				() -> sut.archive(customerId));
	}

	@Test
	void shouldChangeEmail() {
		UUID customerId = sut.create(aCustomerInput().build());

		sut.changeEmail(customerId, "newemail@email.com");

		CustomerOutput result = sut.findById(customerId);
		assertThat(result.getEmail()).isEqualTo("newemail@email.com");
	}

	@Test
	void shouldThrowWhenChangingEmailOfNonExistentCustomer() {
		assertThatThrownBy(() -> sut.changeEmail(UUID.randomUUID(), "any@email.com"))
				.isInstanceOf(CustomerNotFoundException.class);
	}

	@Test
	void shouldThrowWhenChangingEmailOfArchivedCustomer() {
		UUID customerId = sut.create(aCustomerInput().build());
		sut.archive(customerId);

		assertThatThrownBy(() -> sut.changeEmail(customerId, "new@email.com"))
				.isInstanceOf(CustomerArchivedException.class);
	}

	@Test
	void shouldThrowWhenChangingEmailToInvalidFormat() {
		UUID customerId = sut.create(aCustomerInput().build());

		assertThatThrownBy(() -> sut.changeEmail(customerId, "email-invalido"))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void shouldThrowWhenChangingEmailToAlreadyExistingEmail() {
		UUID firstCustomerId = sut.create(aCustomerInput()
				.withEmail("first@email.com")
				.withDocument("111111111")
				.build());

		sut.create(aCustomerInput()
				.withEmail("second@email.com")
				.withDocument("222222222")
				.build());

		assertThatThrownBy(() -> sut.changeEmail(firstCustomerId, "second@email.com"))
				.isInstanceOf(CustomerEmailIsInUseException.class);
	}
}
