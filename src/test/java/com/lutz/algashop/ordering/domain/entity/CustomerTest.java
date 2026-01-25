package com.lutz.algashop.ordering.domain.entity;

import com.lutz.algashop.ordering.domain.exception.CustomerArchivedException;
import com.lutz.algashop.ordering.domain.vo.*;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class CustomerTest {
	private Customer sut;

	CustomerId idStub = new CustomerId();
	FullName fullNameStub = new FullName("John", "Doe");
	Birthdate birthDateStub = new Birthdate(LocalDate.now().minusYears(10));
	Email validEmailStub = new Email("valid@email.com");
	Phone validPhone = new Phone("123123123");
	Document validDocument = new Document("123123123");
	Boolean promotionNotificationAllowedStub = false;
	OffsetDateTime registeredAtStub = OffsetDateTime.now();
	ZipCode validZipCode = new ZipCode("123123");
	Address validAddress = Address.builder()
	                              .street("This street")
	                              .number("102")
	                              .complement("near the thing")
	                              .neighborhood("that one")
	                              .city("Brooklyn")
	                              .state("New York")
	                              .zipCode(validZipCode)
	                              .build();

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
					validAddress,
					promotionNotificationAllowedStub,
					registeredAtStub
			);
		}

		@Test
		void givenUnarchivedUserArchiveShouldAnonymizeData() {
			sut.archive();

			Assertions.assertNotEquals(validEmailStub, sut.email());
			Assertions.assertEquals("Archived -", sut.fullName().toString());
			Assertions.assertEquals(new Phone("0"), sut.phone());
			Assertions.assertEquals(new Document("0").toString(), sut.document().toString());
		}

		@Test
		void givenArchivedUserArchiveShouldThrowCustomerArchivedException() {
			sut.archive();

			Assertions.assertThrows(CustomerArchivedException.class, sut::archive);
		}

		@Test
		void givenArchivedUserAnyChangeShouldThrowCustomerArchivedException() {
			sut.archive();

			Assertions.assertThrows(CustomerArchivedException.class,
					() -> sut.changeEmail(new Email("valid@email.com")));
			Assertions.assertThrows(CustomerArchivedException.class,
					() -> sut.changeName(new FullName("anything", "anything")));
			Assertions.assertThrows(CustomerArchivedException.class, () -> sut.changePhone(new Phone("123123123")));
			Assertions.assertThrows(CustomerArchivedException.class, () -> sut.disablePromotionNotifications());
			Assertions.assertThrows(CustomerArchivedException.class, () -> sut.enablePromotionNotifications());
			Assertions.assertThrows(CustomerArchivedException.class,
					() -> sut.changeAddress(sut.address().toBuilder().build()));
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
					validAddress,
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

			Assertions.assertThrows(IllegalArgumentException.class,
					() -> sut.addLoyaltyPoints(new LoyaltyPoints(newPoints)));
		}
	}
}
