package com.lutz.algashop.ordering.domain.vo;

import com.lutz.algashop.ordering.domain.exception.ErrorMessages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductNameTest {

    @Test
    void shouldCreateProductNameWithValidValue() {
        String validName = "Test Product";
        ProductName productName = new ProductName(validName);
        Assertions.assertEquals(validName, productName.value());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    void shouldThrowExceptionWhenValueIsNullOrBlank(String invalidName) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ProductName(invalidName));
        Assertions.assertEquals(ErrorMessages.Validation.fieldIsNullMessage("Product name"), exception.getMessage());
    }

    @Test
    void toStringShouldReturnValue() {
        String name = "Another Product";
        ProductName productName = new ProductName(name);
        Assertions.assertEquals(name, productName.toString());
    }
}