package com.lutz.algashop.ordering.domain.entity.builder;

import com.lutz.algashop.ordering.domain.entity.customer.Customer;
import com.lutz.algashop.ordering.domain.entity.customer.vo.*;

import java.time.LocalDate;

public class CustomerTestBuilder {

    private CustomerId id = new CustomerId();
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

    public static CustomerTestBuilder aNewCustomer() {
        return new CustomerTestBuilder();
    }

    public static CustomerTestBuilder anExistingCustomer() {
        // Since there's no existing builder in the domain, we'll use a new one for now.
        // If the domain evolves to have an existing builder, we should update this.
        return new CustomerTestBuilder();
    }

    public CustomerTestBuilder withFullName(FullName fullName) {
        this.fullName = fullName;
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
        return Customer.newCustomerBuilder()
                .fullName(fullName)
                .birthDate(birthdate)
                .email(email)
                .phone(phone)
                .document(document)
                .address(address)
                .promotionNotificationAllowed(promotionNotificationAllowed)
                .build();
    }
}
