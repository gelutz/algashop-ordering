package com.lutz.algashop.ordering.domain.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class Customer {
    UUID id;
    String fullName;
    LocalDate birthDate;
    String email;
    String phone;
    String document;
    Boolean promotionNotificationAllowed;
    Boolean archived;
    OffsetDateTime registeredAt;
    OffsetDateTime archivedAt;
    Integer loyaltyPoints;

    // new customer
    public Customer(UUID id, String fullName, LocalDate birthDate, String email, String phone, String document, Boolean promotionNotificationAllowed, OffsetDateTime registeredAt) {
        this.id = id;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
        this.document = document;
        this.promotionNotificationAllowed = promotionNotificationAllowed;
        this.registeredAt = registeredAt;
    }

    // existing customer
    public Customer(UUID id, String fullName, LocalDate birthDate, String email, String phone, String document, Boolean promotionNotificationAllowed, Boolean archived, OffsetDateTime registeredAt, OffsetDateTime archivedAt, Integer loyaltyPoints) {
        this.id = id;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
        this.document = document;
        this.promotionNotificationAllowed = promotionNotificationAllowed;
        this.archived = archived;
        this.registeredAt = registeredAt;
        this.archivedAt = archivedAt;
        this.loyaltyPoints = loyaltyPoints;
    }

    public void addLoyaltyPoints(int points) {};
    public void archive() {};
    public void enablePromotionNotifications() {
        this.promotionNotificationAllowed(true);
    };
    public void disablePromotionNotifications() {
        this.promotionNotificationAllowed(false);
    };

    public void changeName(String fullName) {
        this.fullName(fullName);
    };
    public void changeEmail(String email) {
        this.email(email);
    };
    public void changePhone(String phone) {
        this.phone(phone);
    };
}
