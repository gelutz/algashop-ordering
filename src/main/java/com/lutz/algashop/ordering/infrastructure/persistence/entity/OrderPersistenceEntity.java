package com.lutz.algashop.ordering.infrastructure.persistence.entity;

import com.lutz.algashop.ordering.infrastructure.persistence.entity.embeddable.BillingEmbeddable;
import com.lutz.algashop.ordering.infrastructure.persistence.entity.embeddable.ShippingEmbeddable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString(of = "id")
@Table(name = "\"order\"")
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class OrderPersistenceEntity {
	@Id
	@EqualsAndHashCode.Include
	private Long id;

	@JoinColumn
	@ManyToOne(optional = false)
	private CustomerPersistenceEntity customer;

	private BigDecimal totalAmount;
	private Integer totalItems;
	private String status;
	private String paymentMethod;

	@Embedded
	private BillingEmbeddable billing;
	@Embedded

	private ShippingEmbeddable shipping;

	private OffsetDateTime placedAt;
	private OffsetDateTime paidAt;
	private OffsetDateTime readyAt;
	private OffsetDateTime canceledAt;

	@CreatedBy
	private UUID createdByUserId;
	@LastModifiedDate
	private OffsetDateTime lastModifiedAt;
	@LastModifiedBy
	private UUID lastModifiedUserId;
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<OrderItemPersistenceEntity> items = new HashSet<>();

	@Version
	private Long version;

	@Builder
	public OrderPersistenceEntity(Long id, CustomerPersistenceEntity customer, BigDecimal totalAmount, Integer totalItems, String status, String paymentMethod, BillingEmbeddable billing, ShippingEmbeddable shipping, OffsetDateTime placedAt, OffsetDateTime paidAt, OffsetDateTime readyAt, OffsetDateTime canceledAt, UUID createdByUserId, OffsetDateTime lastModifiedAt, UUID lastModifiedUserId, Long version, Set<OrderItemPersistenceEntity> items) {
		this.id = id;
		this.customer = customer;
		this.totalAmount = totalAmount;
		this.totalItems = totalItems;
		this.status = status;
		this.paymentMethod = paymentMethod;
		this.billing = billing;
		this.shipping = shipping;
		this.placedAt = placedAt;
		this.paidAt = paidAt;
		this.readyAt = readyAt;
		this.canceledAt = canceledAt;
		this.createdByUserId = createdByUserId;
		this.lastModifiedAt = lastModifiedAt;
		this.lastModifiedUserId = lastModifiedUserId;
		this.version = version;
		replaceItems(items);
	}

	public void replaceItems(Set<OrderItemPersistenceEntity> items) {
		if (items == null || items.isEmpty()) {
			setItems(new HashSet<>());
			return;
		}

		items.forEach(i -> i.setOrder(this));
		setItems(items);
	}

	public void addItem(@NonNull OrderItemPersistenceEntity item) {
		item.setOrder(this);

		if (getItems() == null) setItems(new HashSet<>());

		getItems().add(item);
	}

	public UUID getCustomerId() {
		return getCustomer().getId();
	}
}
