package com.lutz.algashop.ordering.domain.entity.order.vo;

import com.lutz.algashop.ordering.domain.entity.order.vo.ProductId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductIdTest {

    @Test
    void shouldCreateProductIdWithGeneratedUUID() {
        ProductId productId = new ProductId();
        assertNotNull(productId.value());
        // Assuming IdGenerator.generateTimeBasedUUID() returns a valid UUID
        Assertions.assertDoesNotThrow(() -> UUID.fromString(productId.value().toString()));
    }

    @Test
    void shouldCreateProductIdWithProvidedUUID() {
        UUID customUUID = UUID.randomUUID();
        ProductId productId = new ProductId(customUUID);
        Assertions.assertEquals(customUUID, productId.value());
    }

    @Test
    void shouldThrowExceptionWhenProvidedUUIDIsNull() {
        assertThrows(NullPointerException.class, () -> new ProductId(null));
    }

    @Test
    void toStringShouldReturnUUIDAsString() {
        UUID customUUID = UUID.randomUUID();
        ProductId productId = new ProductId(customUUID);
        Assertions.assertEquals(customUUID.toString(), productId.toString());
    }
}