package com.lutz.algashop.ordering.domain.entity.customer.vo;

import com.lutz.algashop.ordering.domain.entity.customer.vo.LoyaltyPoints;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LoyaltyPointsTest {
	Integer validPointsStub = 1;
	Integer invalidPointsStub = -1;

	@Test
	void givenValidPointsShouldBeConstructedWithValue() {
		LoyaltyPoints loyaltyPoints = new LoyaltyPoints(validPointsStub);

		Assertions.assertEquals(validPointsStub, loyaltyPoints.value());
	}

	@Test
	void givenValidPointsShouldAddValue() {
		LoyaltyPoints loyaltyPoints = new LoyaltyPoints(validPointsStub);
		loyaltyPoints = loyaltyPoints.add(validPointsStub);

		Assertions.assertEquals(validPointsStub + validPointsStub, loyaltyPoints.value());
	}

	@Test
	void addShouldReturnNewInstance() {
		LoyaltyPoints loyaltyPoints = new LoyaltyPoints(validPointsStub);
		var newLoyaltyPoints = loyaltyPoints.add(validPointsStub);

		Assertions.assertInstanceOf(LoyaltyPoints.class, newLoyaltyPoints);
		Assertions.assertNotEquals(loyaltyPoints, newLoyaltyPoints);
	}

	@Test
	void givenInvalidPointsShouldThrowIllegalArgumentException() {
		LoyaltyPoints loyaltyPoints = new LoyaltyPoints(validPointsStub);
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			loyaltyPoints.add(invalidPointsStub);
		});
	}
}