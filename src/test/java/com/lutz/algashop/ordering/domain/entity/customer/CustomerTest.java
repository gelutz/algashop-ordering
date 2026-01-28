package com.lutz.algashop.ordering.domain.entity.customer;

import com.lutz.algashop.ordering.domain.entity.customer.vo.*;
import com.lutz.algashop.ordering.domain.exception.CustomerArchivedException;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

public class CustomerTest {
	Email validEmailStub = new Email("valid@email.com");
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

	private Customer sut;
	private final Customer.NewCustomerBuilder newCustomerBuilder = Customer.newCustomerBuilder()
	                                                                       .fullName(new FullName("John", "Doe"))
	                                                                       .birthDate(new Birthdate(LocalDate.now().minusYears(10)))
	                                                                       .email(validEmailStub)
	                                                                       .phone(new Phone("123123123"))
	                                                                       .document(new Document("123123123"))
	                                                                       .address(validAddress)
	                                                                       .promotionNotificationAllowed(false);

	@Nested
	@DisplayName("Customer#archive tests")
	class ArchiveTests {
		@BeforeEach
		void setup() {
			sut = newCustomerBuilder.build();
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
	@DisplayName("Customer#addLoyaltyPoints tests")
	class AddLoyaltyPointsTests {
		@BeforeEach
		void setup() {
			sut = newCustomerBuilder.build();
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
