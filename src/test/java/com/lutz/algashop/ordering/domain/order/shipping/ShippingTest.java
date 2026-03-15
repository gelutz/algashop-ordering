package com.lutz.algashop.ordering.domain.order.shipping;

import com.lutz.algashop.ordering.domain.commons.*;
import com.lutz.algashop.ordering.domain.order.Recipient;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

// desativa warning sobre passar null para parametros anotados com @NotNull do lombok
@SuppressWarnings("DataFlowIssue")
class ShippingTest {

    private Address createValidAddress() {
        return new Address("Street", "123", "Neighborhood", "City", "State", "12345-678", new ZipCode("123123"));
    }

    private Recipient createValidRecipient() {
        return Recipient.builder()
                        .fullName(new FullName("John", "Doe"))
                        .document(new Document("123123"))
                        .phone(new Phone("11123"))
                        .build();
    }

    @Test
    void shouldCreateShippingInfoWithAllValidFields() {
        Address address = createValidAddress();
        Shipping shipping = Shipping.builder()
                                    .recipient(createValidRecipient())
                                    .address(address)
                                    .cost(new Money("10"))
                                    .expectedDeliveryDate(LocalDate.now().plusDays(10))
                                    .build();

        assertNotNull(shipping);
        assertNotNull(shipping.recipient());
        assertEquals(address, shipping.address());
        assertEquals(LocalDate.now().plusDays(10), shipping.expectedDeliveryDate());
        assertEquals(new Money("10"), shipping.cost());
    }

    @Test
    void shouldThrowExceptionWhenRequiredFieldIsNull() {
        assertThrows(NullPointerException.class, () -> Shipping.builder()
                                                               .recipient(null)
                                                               .address(createValidAddress())
                                                               .cost(new Money("10"))
                                                               .expectedDeliveryDate(LocalDate.now().plusDays(10))
                                                               .build());

        assertThrows(NullPointerException.class, () -> Shipping.builder()
                                                               .recipient(createValidRecipient())
                                                               .address(createValidAddress())
                                                               .cost(null)
                                                               .expectedDeliveryDate(LocalDate.now().plusDays(10))
                                                               .build());

        assertThrows(NullPointerException.class, () -> Shipping.builder()
                                                               .recipient(createValidRecipient())
                                                               .address(createValidAddress())
                                                               .cost(new Money("10"))
                                                               .expectedDeliveryDate(null)

                                                               .build());

        assertThrows(NullPointerException.class, () -> Shipping.builder()
                                                               .recipient(createValidRecipient())
                                                               .address(null)
                                                               .cost(new Money("10"))
                                                                .expectedDeliveryDate(LocalDate.now().plusDays(10))

                                                               .build());
    }
}