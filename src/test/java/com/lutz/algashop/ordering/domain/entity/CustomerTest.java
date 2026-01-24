package com.lutz.algashop.ordering.domain.entity;

import com.lutz.algashop.ordering.utils.IdGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class CustomerTest {
	UUID idStub = IdGenerator.generateTimeBasedUUID();
	String fullNameStub = "John Doe";
	LocalDate birthDateStub = LocalDate.now().minusDays(1);
	String invalidEmailStub = "invalid-email";
	String validEmailStub = "valid@email.com";
	String validPhone = "123123123";
	String validDocument = "123123123";
	Boolean promotionNotificationAllowedStub = false;
	OffsetDateTime registeredAtStub = OffsetDateTime.now();

	@Test
	void givenInvalidEmailConstructorShouldThrowIllegalArgumentException() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Customer(
					idStub,
					fullNameStub,
					birthDateStub,
					invalidEmailStub,
					validPhone,
					validDocument,
					promotionNotificationAllowedStub,
					registeredAtStub
			);
		});
	}

	@Test
	void givenValidEmailChangeEmailShouldThrowIllegalArgumentException() {
		Customer customer = new Customer(
				idStub,
				fullNameStub,
				birthDateStub,
				validEmailStub,
				validPhone,
				validDocument,
				promotionNotificationAllowedStub,
				registeredAtStub
		);

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			customer.changeEmail(invalidEmailStub);
		});
	}

}
