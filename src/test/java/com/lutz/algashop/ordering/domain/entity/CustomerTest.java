package com.lutz.algashop.ordering.domain.entity;

import com.lutz.algashop.ordering.domain.exception.CustomerArchivedException;
import com.lutz.algashop.ordering.domain.vo.CustomerId;
import com.lutz.algashop.ordering.domain.vo.FullName;

import com.lutz.algashop.ordering.domain.vo.LoyaltyPoints;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class CustomerTest {
	private Customer sut;

	CustomerId idStub = new CustomerId();
	FullName fullNameStub = new FullName("John", "Doe");
	LocalDate birthDateStub = LocalDate.now().minusYears(10);
	String invalidEmailStub = "invalid-email";
	String validEmailStub = "valid@email.com";
	String validPhone = "123123123";
	String validDocument = "123123123";
	Boolean promotionNotificationAllowedStub = false;
	OffsetDateTime registeredAtStub = OffsetDateTime.now();

	@Nested
	@DisplayName("Customer constructor and setters tests")
	class SettersTests {
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

	@Nested
	@DisplayName("Customer#archive tests")
	class ArchiveTests {
		@BeforeEach
		void setup() {
			sut = new Customer(
					idStub,
					fullNameStub,
					birthDateStub,
					validEmailStub,
					validPhone,
					validDocument,
					promotionNotificationAllowedStub,
					registeredAtStub
			);
		}

		@Test
		void givenUnarchivedUserArchiveShouldAnonymizeData() {
			sut.archive();

			Assertions.assertNotEquals(validEmailStub, sut.email());
			Assertions.assertEquals("Archived -", sut.fullName().toString());
			Assertions.assertEquals("0", sut.phone()); // will break when phone validations are added to model
			Assertions.assertEquals("0", sut.document()); // will break when document validations are added to model
		}

		@Test
		void givenArchivedUserArchiveShouldThrowCustomerArchivedException() {
			sut.archive();

			Assertions.assertThrows(CustomerArchivedException.class, sut::archive);
		}

		@Test
		void givenArchivedUserAnyChangeShouldThrowCustomerArchivedException() {
			sut.archive();

			Assertions.assertThrows(CustomerArchivedException.class, () -> sut.changeEmail("anything"));
			Assertions.assertThrows(CustomerArchivedException.class, () -> sut.changeName(new FullName("anything", "anything")));
			Assertions.assertThrows(CustomerArchivedException.class, () -> sut.changePhone("anything"));
			Assertions.assertThrows(CustomerArchivedException.class, () -> sut.disablePromotionNotifications());
			Assertions.assertThrows(CustomerArchivedException.class, () -> sut.enablePromotionNotifications());
		}
	}

	@Nested
	class LoyaltyPointsTests {
		@BeforeEach
		void setup() {
			sut = new Customer(
					idStub,
					fullNameStub,
					birthDateStub,
					validEmailStub,
					validPhone,
					validDocument,
					promotionNotificationAllowedStub,
					registeredAtStub
			);
		}

		@Test
		void givenValidCustomerAndValidPointsAddLoyaltyPointsShouldSumPoints() {
			int newPoints = 10;
			int initial = sut.loyaltyPoints().value();

			sut.addLoyaltyPoints(new LoyaltyPoints(newPoints));

			Assertions.assertEquals(initial + newPoints, sut.loyaltyPoints().value());
		}

		@Test
		void givenValidCustomerAndInvalidPointsAddLoyaltyPointsShouldThrowIllegalArgumentException() {
			int newPoints = 0;

			Assertions.assertThrows(IllegalArgumentException.class, () -> sut.addLoyaltyPoints(new LoyaltyPoints(newPoints)));
		}
	}
}
