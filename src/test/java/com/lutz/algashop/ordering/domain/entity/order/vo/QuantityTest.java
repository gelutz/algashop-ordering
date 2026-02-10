package com.lutz.algashop.ordering.domain.entity.order.vo;

import com.lutz.algashop.ordering.domain.exception.messages.ErrorMessages;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuantityTest {

    @Test
    void shouldCreateQuantityWithValidPositiveValue() {
        Quantity quantity = new Quantity(5);
        assertEquals(5, quantity.value());
    }

    @Test
    void shouldCreateQuantityWithZeroValue() {
        Quantity quantity = new Quantity(0);
        assertEquals(0, quantity.value());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new Quantity(null));
        assertEquals(ErrorMessages.Fields.VALUE_IS_NULL, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNegative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Quantity(-1));
        assertEquals(ErrorMessages.Fields.VALUE_IS_NEGATIVE, exception.getMessage());
    }

    @Test
    void zeroConstantShouldHaveValueZero() {
        assertEquals(new Quantity(0), Quantity.ZERO);
    }

    @Test
    void cloneShouldReturnAnEqualButDifferentObject() {
        Quantity original = new Quantity(10);
        Quantity cloned = original.clone();

        assertEquals(original, cloned);
        assertNotSame(original, cloned);
    }

    @Test
    void compareToShouldReturnCorrectComparison() {
        Quantity q1 = new Quantity(5);
        Quantity q2 = new Quantity(10);
        Quantity q3 = new Quantity(5);

        assertTrue(q1.compareTo(q2) < 0);
        assertTrue(q2.compareTo(q1) > 0);
        assertEquals(0, q1.compareTo(q3));
    }
}