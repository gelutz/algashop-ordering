package com.lutz.algashop.ordering.domain.entity.customer;

import com.lutz.algashop.ordering.domain.entity.AggregateRoot;
import com.lutz.algashop.ordering.domain.entity.customer.vo.*;
import com.lutz.algashop.ordering.domain.exception.CustomerArchivedException;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
@EqualsAndHashCode(of = "id")
public class Customer implements AggregateRoot<CustomerId> {
	private CustomerId id;
	private FullName fullName;
	private Birthdate birthdate;
	private Email email;
	private Phone phone;
	private Document document;
	private Address address;
	private Boolean promotionNotificationAllowed;
	private Boolean archived;
	private OffsetDateTime registeredAt;
	private OffsetDateTime archivedAt;
	private LoyaltyPoints loyaltyPoints;
	private Long version;

	// existing customer
	@Builder(builderClassName = "ExistingCustomerBuilder", builderMethodName = "existing")
	private Customer(CustomerId id, FullName fullName, Birthdate birthDate, Email email, Phone phone, Document document, Address address, Boolean promotionNotificationAllowed, Boolean archived, OffsetDateTime registeredAt, OffsetDateTime archivedAt, LoyaltyPoints loyaltyPoints, Long version) {
		setId(id);
		setFullName(fullName);
		this.birthdate = birthDate;
		setEmail(email);
		setPhone(phone);
		setDocument(document);
		setAddress(address);
		setPromotionNotificationAllowed(promotionNotificationAllowed);
		setArchived(archived);
		setRegisteredAt(registeredAt);
		setArchivedAt(archivedAt);
		setLoyaltyPoints(loyaltyPoints);
		setVersion(version);
	}

	// new customer
	@Builder(builderClassName = "NewCustomerBuilder", builderMethodName = "fresh")
	private Customer(FullName fullName, Birthdate birthDate, Email email, Phone phone, Document document, Address address, Boolean promotionNotificationAllowed) {
		setId(new CustomerId());
		setFullName(fullName);
		setBirthdate(birthDate.date());
		setEmail(email);
		setPhone(phone);
		setDocument(document);
		setAddress(address);
		setPromotionNotificationAllowed(promotionNotificationAllowed);
		setArchived(false);
		setRegisteredAt(OffsetDateTime.now());
		setArchivedAt(null);
		setLoyaltyPoints(LoyaltyPoints.ZERO);
		setVersion(null);
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

		this.setAddress(address()
				.toBuilder()
				.street("anon")
				.number("000")
				.city("anon")
				.state("anon")
				.build());
	}

	public void addLoyaltyPoints(LoyaltyPoints points) {
		canChange();
		if (points.value() == 0)
			throw new IllegalArgumentException();
		this.setLoyaltyPoints(this.loyaltyPoints().add(points));
	}

	;

	public void enablePromotionNotifications() {
		canChange();
		this.setPromotionNotificationAllowed(true);
	}

	;

	public void disablePromotionNotifications() {
		canChange();
		this.setPromotionNotificationAllowed(false);
	}

	;

	public void changeName(FullName fullName) {
		canChange();
		this.setFullName(fullName);
	}

	;

	public void changeEmail(Email email) {
		canChange();
		this.setEmail(email);
	}

	;

	public void changePhone(Phone phone) {
		canChange();
		this.setPhone(phone);
	}

	;

	public void changeAddress(Address address) {
		canChange();
		this.setAddress(address);
	}

	public Long version() {
		return version;
	}

	private void setId(CustomerId id) {
		this.id = id;
	}

	private void setPhone(Phone phone) {
		this.phone = phone;
	}

	private void setDocument(Document document) {
		this.document = document;
	}

	private void setAddress(@NonNull Address address) {
		this.address = address;
	}

	private void setPromotionNotificationAllowed(@NonNull Boolean promotionNotificationAllowed) {
		this.promotionNotificationAllowed = promotionNotificationAllowed;
	}

	private void setArchived(@NonNull Boolean archived) {
		this.archived = archived;
	}

	private void setRegisteredAt(@NonNull OffsetDateTime registeredAt) {
		this.registeredAt = registeredAt;
	}

	private void setArchivedAt(OffsetDateTime archivedAt) {
		this.archivedAt = archivedAt;
	}

	private void setLoyaltyPoints(LoyaltyPoints loyaltyPoints) {
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

	private void setVersion(Long version) {
		this.version = version;
	}

	private void canChange() {
		if (archived())
			throw new CustomerArchivedException();
	}
}
