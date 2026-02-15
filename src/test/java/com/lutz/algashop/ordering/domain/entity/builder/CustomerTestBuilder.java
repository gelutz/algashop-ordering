package com.lutz.algashop.ordering.domain.entity.builder;

import com.lutz.algashop.ordering.domain.entity.customer.Customer;
import com.lutz.algashop.ordering.domain.entity.customer.vo.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class CustomerTestBuilder {
    public static final CustomerId DEFAULT_CUSTOMER_ID = new CustomerId();

    private CustomerId id = DEFAULT_CUSTOMER_ID;
    private FullName fullName = new FullName("John", "Doe");
    private Birthdate birthdate = new Birthdate(LocalDate.now().minusYears(10));
    private Email email = new Email("valid@email.com");
    private Phone phone = new Phone("123123123");
    private Document document = new Document("123123123");
    private Address address = Address.builder()
            .street("This street")
            .number("102")
            .complement("near the thing")
            .neighborhood("that one")
            .city("Brooklyn")
            .state("New York")
            .zipCode(new ZipCode("123123"))
            .build();
    private Boolean promotionNotificationAllowed = false;

    private CustomerTestBuilder() {
    }

    public static CustomerTestBuilder aCustomer() {
        return new CustomerTestBuilder();
    }

    public CustomerTestBuilder withId(CustomerId id) {
        this.id = id;
        return this;
    }

    public CustomerTestBuilder withBirthdate(Birthdate birthdate) {
        this.birthdate = birthdate;
        return this;
    }

    public CustomerTestBuilder withEmail(Email email) {
        this.email = email;
        return this;
    }

    public CustomerTestBuilder withPhone(Phone phone) {
        this.phone = phone;
        return this;
    }

    public CustomerTestBuilder withDocument(Document document) {
        this.document = document;
        return this;
    }

    public CustomerTestBuilder withAddress(Address address) {
        this.address = address;
        return this;
    }

    public CustomerTestBuilder withPromotionNotificationAllowed(Boolean allowed) {
        this.promotionNotificationAllowed = allowed;
        return this;
    }

    public CustomerTestBuilder with() {
        return this;
    }

    public Customer build() {
        // The domain only exposes NewCustomerBuilder which sets ID, registeredAt, etc.
        return Customer.existing()
                       .id(id)
                       .fullName(fullName)
                       .birthDate(birthdate)
                       .email(email)
                       .phone(phone)
                       .document(document)
                       .address(address)
                       .promotionNotificationAllowed(promotionNotificationAllowed)
                       .archived(false)
                       .registeredAt(OffsetDateTime.now())
                       .loyaltyPoints(LoyaltyPoints.ZERO)
                       .build();
    }
}
