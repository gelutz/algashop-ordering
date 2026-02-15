package com.lutz.algashop.ordering.infrastructure.persistence.entity;

import com.lutz.algashop.ordering.infrastructure.persistence.entity.embeddable.AddressEmbeddable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString(of = "id")
@Table(name = "customer")
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class CustomerPersistenceEntity {
	@Id
	@EqualsAndHashCode.Include
	private UUID id;

	private String firstName;
	private String lastName;
	private LocalDate birthdate;
	private String email;
	private String phone;
	private String document;
	private Integer loyaltyPoints;
	private Boolean promotionNotificationAllowed;
	private Boolean archived;
	private OffsetDateTime registeredAt;
	private OffsetDateTime archivedAt;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "street", column = @Column(name = "address_street")),
			@AttributeOverride(name = "number", column = @Column(name = "address_number")),
			@AttributeOverride(name = "complement", column = @Column(name = "address_complement")),
			@AttributeOverride(name = "neighborhood", column = @Column(name = "address_neighborhood")),
			@AttributeOverride(name = "city", column = @Column(name = "address_city")),
			@AttributeOverride(name = "state", column = @Column(name = "address_state")),
			@AttributeOverride(name = "zipCode", column = @Column(name = "address_zipCode"))
	})
	private AddressEmbeddable address;

	@CreatedBy
	private UUID createdByUserId;
	@LastModifiedDate
	private OffsetDateTime lastModifiedAt;
	@LastModifiedBy
	private UUID lastModifiedUserId;

	@Version
	private Long version;

	@Builder
	public CustomerPersistenceEntity(UUID id, String firstName, String lastName, LocalDate birthdate, String email, String phone, String document, Integer loyaltyPoints, Boolean promotionNotificationAllowed, Boolean archived, OffsetDateTime registeredAt, OffsetDateTime archivedAt, AddressEmbeddable address, UUID createdByUserId, OffsetDateTime lastModifiedAt, UUID lastModifiedUserId, Long version) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthdate = birthdate;
		this.email = email;
		this.phone = phone;
		this.document = document;
		this.loyaltyPoints = loyaltyPoints;
		this.promotionNotificationAllowed = promotionNotificationAllowed;
		this.archived = archived;
		this.registeredAt = registeredAt;
		this.archivedAt = archivedAt;
		this.address = address;
		this.createdByUserId = createdByUserId;
		this.lastModifiedAt = lastModifiedAt;
		this.lastModifiedUserId = lastModifiedUserId;
		this.version = version;
	}
}
