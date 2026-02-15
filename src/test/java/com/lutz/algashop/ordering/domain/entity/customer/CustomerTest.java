package com.lutz.algashop.ordering.domain.entity.customer;

import com.lutz.algashop.ordering.domain.entity.builder.CustomerTestBuilder;
import com.lutz.algashop.ordering.domain.entity.customer.vo.*;
import com.lutz.algashop.ordering.domain.exception.CustomerArchivedException;
import org.junit.jupiter.api.*;

public class CustomerTest {
	private Customer sut;
	private final Email validEmailStub = new Email("valid@email.com");

	@Nested
	@DisplayName("Customer#archive tests")
	class ArchiveTests {
		@BeforeEach
		void setup() {
			sut = CustomerTestBuilder.aCustomer().build();
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
			sut = CustomerTestBuilder.aCustomer().build();
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
