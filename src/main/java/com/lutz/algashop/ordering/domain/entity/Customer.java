package com.lutz.algashop.ordering.domain.entity;

import com.lutz.algashop.ordering.domain.exception.CustomerArchivedException;
import com.lutz.algashop.ordering.domain.exception.ErrorMessages;
import com.lutz.algashop.ordering.utils.Validation;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
@EqualsAndHashCode(of = "id")
public class Customer {
    UUID id;
    String fullName;
    LocalDate birthdate;
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
        setId(id);
        setFullName(fullName);
        setBirthdate(birthDate);
        setEmail(email);
        setPhone(phone);
        setDocument(document);
        setPromotionNotificationAllowed(promotionNotificationAllowed);
        setRegisteredAt(registeredAt);
        setArchived(false);
        setLoyaltyPoints(0);
    }

    // existing customer
    public Customer(UUID id, String fullName, LocalDate birthDate, String email, String phone, String document, Boolean promotionNotificationAllowed, Boolean archived, OffsetDateTime registeredAt, OffsetDateTime archivedAt, Integer loyaltyPoints) {
        setId(id);
        setFullName(fullName);
        setBirthdate(birthDate);
        setEmail(email);
        setPhone(phone);
        setDocument(document);
        setPromotionNotificationAllowed(promotionNotificationAllowed);
        setArchived(archived);
        setRegisteredAt(registeredAt);
        setArchivedAt(archivedAt);
        setLoyaltyPoints(loyaltyPoints);
    }

    public void archive() {
        this.setFullName("Archived");
        this.setEmail(UUID.randomUUID() + "@archived.com");
        this.setPhone("0");
        this.setDocument("0");
        this.setBirthdate(null);
        this.setArchived(true);
        this.setArchivedAt(OffsetDateTime.now());
        this.setPromotionNotificationAllowed(false);
    }

    public void addLoyaltyPoints(int points) {
    };
    public void enablePromotionNotifications() {
        canChange();
        this.setPromotionNotificationAllowed(true);
    };
    public void disablePromotionNotifications() {
        canChange();
        this.setPromotionNotificationAllowed(false);
    };
    public void changeName(String fullName) {
        canChange();
        this.setFullName(fullName);
    };
    public void changeEmail(String email) {
        canChange();
        this.setEmail(email);
    };
    public void changePhone(String phone) {
        canChange();
        this.setPhone(phone);
    };

    public void setId(UUID id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    public void setPhone(String phone) {
        Objects.requireNonNull(phone);
        this.phone = phone;
    }

    public void setDocument(String document) {
        Objects.requireNonNull(document);
        this.document = document;
    }

    public void setPromotionNotificationAllowed(Boolean promotionNotificationAllowed) {
        Objects.requireNonNull(promotionNotificationAllowed);
        this.promotionNotificationAllowed = promotionNotificationAllowed;
    }

    public void setArchived(Boolean archived) {
        Objects.requireNonNull(archived);
        this.archived = archived;
    }

    public void setRegisteredAt(OffsetDateTime registeredAt) {
        Objects.requireNonNull(registeredAt);
        this.registeredAt = registeredAt;
    }

    public void setArchivedAt(OffsetDateTime archivedAt) {
        this.archivedAt = archivedAt;
    }

    public void setLoyaltyPoints(Integer loyaltyPoints) {
        Objects.requireNonNull(loyaltyPoints);
        this.loyaltyPoints = loyaltyPoints;
    }


    private void setFullName(String fullName) {
        Objects.requireNonNull(fullName, ErrorMessages.Validation.FULL_NAME_IS_NULL);
        if (fullName.isBlank()) throw new IllegalArgumentException(ErrorMessages.Validation.FULL_NAME_IS_BLANK);

        this.fullName = fullName;
    }

    private void setBirthdate(LocalDate birthdate) {
        if (birthdate == null) {
            this.birthdate = null;
            return;
        }

        if (birthdate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(ErrorMessages.Validation.BIRTHDATE_MUST_IN_PAST);
        }

        this.birthdate = birthdate;
    }

    private void setEmail(String email) {
        Validation.requireValidEmail(email);
        this.email = email;
    }

    private void canChange() {
        if (archived()) throw new CustomerArchivedException();
    }
}
