package com.lutz.algashop.ordering.domain.vo;

import com.lutz.algashop.ordering.domain.entity.customer.vo.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BillingInfoTest {

    private FullName createValidFullName() {
        return new FullName("Jane", "Doe");
    }

    private Document createValidDocument() {
        return new Document("12345678901");
    }

    private Phone createValidPhone() {
        return new Phone("21998765432");
    }

    private Address createValidAddress() {
        return new Address("Billing Street", "456", "Billing Neighborhood", "Billing City", "Billing State", "98765-432", new ZipCode("123123123"));
    }

    @Test
    void shouldCreateBillingInfoWithAllValidFields() {
        FullName fullName = createValidFullName();
        Document document = createValidDocument();
        Phone phone = createValidPhone();
        Address address = createValidAddress();

        BillingInfo billingInfo = BillingInfo.builder()
                .fullName(fullName)
                .document(document)
                .phone(phone)
                .address(address)
                .build();

        assertNotNull(billingInfo);
        assertEquals(fullName, billingInfo.fullName());
        assertEquals(document, billingInfo.document());
        assertEquals(phone, billingInfo.phone());
        assertEquals(address, billingInfo.address());
    }

    @Test
    void shouldThrowExceptionWhenRequiredFieldIsNull() {
        assertThrows(NullPointerException.class, () -> BillingInfo.builder()
                .fullName(null)
                .document(createValidDocument())
                .phone(createValidPhone())
                .address(createValidAddress())
                .build());

        assertThrows(NullPointerException.class, () -> BillingInfo.builder()
                .fullName(createValidFullName())
                .document(null)
                .phone(createValidPhone())
                .address(createValidAddress())
                .build());

        assertThrows(NullPointerException.class, () -> BillingInfo.builder()
                .fullName(createValidFullName())
                .document(createValidDocument())
                .phone(null)
                .address(createValidAddress())
                .build());

        assertThrows(NullPointerException.class, () -> BillingInfo.builder()
                .fullName(createValidFullName())
                .document(createValidDocument())
                .phone(createValidPhone())
                .address(null)
                .build());
    }
}