package com.lutz.algashop.ordering.domain.entity.customer.order.vo;

import com.lutz.algashop.ordering.domain.entity.order.vo.ProductName;
import com.lutz.algashop.ordering.domain.exception.ErrorMessages;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductNameTest {

    @Test
    void shouldCreateProductNameWithValidValue() {
        String validName = "Test Product";
        ProductName productName = new ProductName(validName);
        Assertions.assertEquals(validName, productName.value());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new ProductName(null));
        Assertions.assertEquals(ErrorMessages.Fields.fieldIsNullMessage("Product name"), exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenValueIsBlank() {
        String invalidName = "";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ProductName(invalidName));
        Assertions.assertEquals(ErrorMessages.Fields.fieldIsNullMessage("Product name"), exception.getMessage());
    }

    @Test
    void toStringShouldReturnValue() {
        String name = "Another Product";
        ProductName productName = new ProductName(name);
        Assertions.assertEquals(name, productName.toString());
    }
}