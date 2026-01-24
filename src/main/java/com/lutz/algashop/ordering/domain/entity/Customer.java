package com.lutz.algashop.ordering.domain.entity;

import com.lutz.algashop.ordering.domain.exception.CustomerArchivedException;
import com.lutz.algashop.ordering.domain.vo.*;
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
    private CustomerId id;
    private FullName fullName;
    private Birthdate birthdate;
    private Email email;
    private Phone phone;
    private Document document;
    private Boolean promotionNotificationAllowed;
    private Boolean archived;
    private OffsetDateTime registeredAt;
    private OffsetDateTime archivedAt;
    private LoyaltyPoints loyaltyPoints;

    // new customer
    public Customer(CustomerId id, FullName fullName, Birthdate birthDate, Email email, Phone phone, Document document, Boolean promotionNotificationAllowed, OffsetDateTime registeredAt) {
        setId(id);
        setFullName(fullName);
        setBirthdate(birthDate.date());
        setEmail(email);
        setPhone(phone);
        setDocument(document);
        setPromotionNotificationAllowed(promotionNotificationAllowed);
        setRegisteredAt(registeredAt);
        setArchived(false);
        setLoyaltyPoints(LoyaltyPoints.ZERO);
    }

    // existing customer
    public Customer(CustomerId id, FullName fullName, Birthdate birthDate, Email email, Phone phone, Document document, Boolean promotionNotificationAllowed, Boolean archived, OffsetDateTime registeredAt, OffsetDateTime archivedAt, LoyaltyPoints loyaltyPoints) {
        setId(id);
        setFullName(fullName);
        setBirthdate(birthDate.date());
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
        canChange();

        this.setFullName(new FullName("Archived", "-"));
        this.setEmail(new Email(UUID.randomUUID() + "@archived.com"));
        this.setPhone(new Phone("0"));
        this.setDocument(new Document("0"));
        this.setBirthdate(null);
        this.setArchived(true);
        this.setArchivedAt(OffsetDateTime.now());
        this.setPromotionNotificationAllowed(false);
    }

    public void addLoyaltyPoints(LoyaltyPoints points) {
        canChange();
        if (points.value() == 0) throw new IllegalArgumentException();
        this.setLoyaltyPoints(this.loyaltyPoints().add(points));
    };
    public void enablePromotionNotifications() {
        canChange();
        this.setPromotionNotificationAllowed(true);
    };
    public void disablePromotionNotifications() {
        canChange();
        this.setPromotionNotificationAllowed(false);
    };
    public void changeName(FullName fullName) {
        canChange();
        this.setFullName(fullName);
    };
    public void changeEmail(Email email) {
        canChange();
        this.setEmail(email);
    };
    public void changePhone(Phone phone) {
        canChange();
        this.setPhone(phone);
    };

    public void setId(CustomerId id) {
        this.id = id;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public void setDocument(Document document) {
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

    public void setLoyaltyPoints(LoyaltyPoints loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }


    private void setFullName(FullName fullName) {
        this.fullName = fullName;
    }

    private void setBirthdate(LocalDate birthdate) {
        if (birthdate == null) {
            this.birthdate = null;
            return;
        }

        this.birthdate = new Birthdate(birthdate);
    }

    private void setEmail(Email email) {
        this.email = email;
    }

    private void canChange() {
        if (archived()) throw new CustomerArchivedException();
    }
}
