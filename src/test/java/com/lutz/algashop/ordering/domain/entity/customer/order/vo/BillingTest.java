package com.lutz.algashop.ordering.domain.entity.customer.order.vo;

import com.lutz.algashop.ordering.domain.entity.customer.vo.*;
import com.lutz.algashop.ordering.domain.entity.order.vo.Billing;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("DataFlowIssue")
class BillingTest {

    private FullName createValidFullName() {
        return new FullName("Jane", "Doe");
    }

    private Document createValidDocument() {
        return new Document("12345678901");
    }

    private Phone createValidPhone() {
        return new Phone("21998765432");
    }

    private Email createValidEmail() {
        return new Email("teste@teste.com");
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
        Email email = createValidEmail();

        Billing billing = Billing.builder()
                                 .fullName(fullName)
                                 .document(document)
                                 .phone(phone)
                                 .address(address)
                .email(email)
                                 .build();

        assertNotNull(billing);
        assertEquals(fullName, billing.fullName());
        assertEquals(document, billing.document());
        assertEquals(phone, billing.phone());
        assertEquals(address, billing.address());
    }

    @Test
    void shouldThrowExceptionWhenRequiredFieldIsNull() {
        assertThrows(NullPointerException.class, () -> Billing.builder()
                                                              .fullName(null)
                                                              .document(createValidDocument())
                                                              .phone(createValidPhone())
                                                              .email(createValidEmail())
                                                              .address(createValidAddress())
                                                              .build());

        assertThrows(NullPointerException.class, () -> Billing.builder()
                                                              .fullName(createValidFullName())
                                                              .document(null)
                                                              .phone(createValidPhone())
                                                              .email(createValidEmail())
                                                              .address(createValidAddress())
                                                              .build());

        assertThrows(NullPointerException.class, () -> Billing.builder()
                                                              .fullName(createValidFullName())
                                                              .document(createValidDocument())
                                                              .phone(null)
                                                              .email(createValidEmail())
                                                              .address(createValidAddress())
                                                              .build());

        assertThrows(NullPointerException.class, () -> Billing.builder()
                                                              .fullName(createValidFullName())
                                                              .document(createValidDocument())
                                                              .phone(createValidPhone())
                                                              .email(null)
                                                              .address(createValidAddress())
                                                              .build());

        assertThrows(NullPointerException.class, () -> Billing.builder()
                                                              .fullName(createValidFullName())
                                                              .document(createValidDocument())
                                                              .phone(createValidPhone())
                                                              .email(createValidEmail())
                                                              .address(null)
                                                              .build());
    }
}