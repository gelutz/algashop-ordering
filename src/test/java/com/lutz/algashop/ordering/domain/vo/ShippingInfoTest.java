package com.lutz.algashop.ordering.domain.vo;

import com.lutz.algashop.ordering.domain.entity.customer.vo.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

// desativa warning sobre passar null para parametros anotados com @NotNull do lombok
@SuppressWarnings("DataFlowIssue")
class ShippingInfoTest {

    private FullName createValidFullName() {
        return new FullName("John", "Doe");
    }

    private Document createValidDocument() {
        return new Document("12345678901");
    }

    private Phone createValidPhone() {
        return new Phone("11987654321");
    }

    private Address createValidAddress() {
        return new Address("Street", "123", "Neighborhood", "City", "State", "12345-678", new ZipCode("123123"));
    }

    @Test
    void shouldCreateShippingInfoWithAllValidFields() {
        FullName fullName = createValidFullName();
        Document document = createValidDocument();
        Phone phone = createValidPhone();
        Address address = createValidAddress();

        ShippingInfo shippingInfo = ShippingInfo.builder()
                .fullName(fullName)
                .document(document)
                .phone(phone)
                .address(address)
                .build();

        assertNotNull(shippingInfo);
        Assertions.assertEquals(fullName, shippingInfo.fullName());
        Assertions.assertEquals(document, shippingInfo.document());
        Assertions.assertEquals(phone, shippingInfo.phone());
        Assertions.assertEquals(address, shippingInfo.address());
    }

    @Test
    void shouldThrowExceptionWhenFullNameIsNull() {
        assertThrows(NullPointerException.class, () -> ShippingInfo.builder()
                .fullName(null)
                .document(createValidDocument())
                .phone(createValidPhone())
                .address(createValidAddress())
                .build());
    }

    @Test
    void shouldThrowExceptionWhenDocumentIsNull() {
        assertThrows(NullPointerException.class, () -> ShippingInfo.builder()
                .fullName(createValidFullName())
                .document(null)
                .phone(createValidPhone())
                .address(createValidAddress())
                .build());
    }

    @Test
    void shouldThrowExceptionWhenPhoneIsNull() {
        assertThrows(NullPointerException.class, () -> ShippingInfo.builder()
                .fullName(createValidFullName())
                .document(createValidDocument())
                .phone(null)
                .address(createValidAddress())
                .build());
    }

    @Test
    void shouldThrowExceptionWhenAddressIsNull() {
        assertThrows(NullPointerException.class, () -> ShippingInfo.builder()
                .fullName(createValidFullName())
                .document(createValidDocument())
                .phone(createValidPhone())
                .address(null)
                .build());
    }
}