package com.lutz.algashop.ordering.application.customer.management;

import com.lutz.algashop.ordering.application.commons.AddressData;
import com.lutz.algashop.ordering.application.customer.notification.CustomerNotificationApplicationService;
import com.lutz.algashop.ordering.domain.customer.*;
import com.lutz.algashop.ordering.infrastructure.listener.customer.CustomerEventListener;
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
	private CustomerNotificationApplicationService customerNotificationApplicationService;

	@Test
	void shouldRegister() {
		var customer = aCustomerInput().build();
		UUID resultingId = sut.create(customer);

		assertThat(resultingId).isNotNull();

		CustomerOutput resultingOutput = sut.findById(resultingId);

		assertThat(resultingOutput.getFirstName()).isEqualTo(customer.getFirstName());
		assertThat(resultingOutput.getLastName()).isEqualTo(customer.getLastName());
		assertThat(resultingOutput.getEmail()).isEqualTo(customer.getEmail());
		assertThat(resultingOutput.getPhone()).isEqualTo(customer.getPhone());
		assertThat(resultingOutput.getDocument()).isEqualTo(customer.getDocument());
		assertThat(resultingOutput.getBirthdate()).isEqualTo(customer.getBirthdate());

		Mockito.verify(customerEventListener).listen(Mockito.any(CustomerRegisteredEvent.class));

		Mockito.verify(customerNotificationApplicationService).notifyNewRegistration(Mockito.any(
				CustomerNotificationApplicationService.NotifyNewRegistrationInput.class));
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

		assertThat(updated.getFirstName()).isEqualTo("Jane");
		assertThat(updated.getLastName()).isEqualTo("Smith");
		assertThat(updated.getPhone()).isEqualTo("999888777");
		assertThat(updated.getPromotionNotificationAllowed()).isTrue();
		assertThat(updated.getAddress().getStreet()).isEqualTo("New street");
		assertThat(updated.getAddress().getNumber()).isEqualTo("200");
		assertThat(updated.getAddress().getComplement()).isEqualTo("apt 5");
		assertThat(updated.getAddress().getNeighborhood()).isEqualTo("downtown");
		assertThat(updated.getAddress().getCity()).isEqualTo("Manhattan");
		assertThat(updated.getAddress().getState()).isEqualTo("New York");
		assertThat(updated.getAddress().getZipCode()).isEqualTo("456456");
	}

	@Test
	void shouldThrowWhenUpdatingNonExistentCustomer() {
		CustomerUpdateInput updateInput = aCustomerUpdateInput().build();

		assertThatThrownBy(() -> sut.update(UUID.randomUUID(), updateInput))
				.isInstanceOf(CustomerNotFoundException.class);
	}

	@Test
	void shouldArchiveCustomer() {
		var input = aCustomerInput().build();
		UUID customerId = sut.create(input);

		sut.archive(customerId);

		CustomerOutput result = sut.findById(customerId);

		assertThat(result.getArchived()).isTrue();
		assertThat(result.getArchivedAt()).isNotNull();
		assertThat(result.getFirstName()).isEqualTo("Archived");
		assertThat(result.getLastName()).isEqualTo("-");
		assertThat(result.getEmail()).endsWith("@archived.com");
		assertThat(result.getPhone()).isEqualTo("0");
		assertThat(result.getDocument()).isEqualTo("0");
		assertThat(result.getBirthdate()).isNull();
		assertThat(result.getPromotionNotificationAllowed()).isFalse();
		assertThat(result.getAddress().getNumber()).isEqualTo("000");
		assertThat(result.getAddress().getStreet()).isEqualTo("anon");
		assertThat(result.getAddress().getCity()).isEqualTo("anon");
		assertThat(result.getAddress().getState()).isEqualTo("anon");
	}

	@Test
	void shouldThrowWhenArchivingNonExistentCustomer() {
		assertThatThrownBy(() -> sut.archive(UUID.randomUUID()))
				.isInstanceOf(CustomerNotFoundException.class);
	}

	@Test
	void shouldThrowWhenArchivingAlreadyArchivedCustomer() {
		UUID customerId = sut.create(aCustomerInput().build());
		sut.archive(customerId);

		assertThatThrownBy(() -> sut.archive(customerId))
				.isInstanceOf(CustomerArchivedException.class);
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
