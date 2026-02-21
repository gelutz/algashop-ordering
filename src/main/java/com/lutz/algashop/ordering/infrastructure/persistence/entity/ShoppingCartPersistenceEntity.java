package com.lutz.algashop.ordering.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
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
@Table(name = "shopping_cart")
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class ShoppingCartPersistenceEntity {
	@Id
	@EqualsAndHashCode.Include
	private UUID id;

	@JoinColumn
	@ManyToOne(optional = false)
	private CustomerPersistenceEntity customer;

	private BigDecimal totalAmount;
	private Integer totalItems;
	private OffsetDateTime createdAt;

	@CreatedBy
	private UUID createdByUserId;
	@CreatedDate
	private OffsetDateTime createdAtAudit;
	@LastModifiedDate
	private OffsetDateTime lastModifiedAt;
	@LastModifiedBy
	private UUID lastModifiedByUserId;

	@Version
	private Long version;

	@OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ShoppingCartItemPersistenceEntity> items = new HashSet<>();

	@Builder
	public ShoppingCartPersistenceEntity(UUID id, CustomerPersistenceEntity customer, BigDecimal totalAmount, Integer totalItems, OffsetDateTime createdAt, UUID createdByUserId, OffsetDateTime createdAtAudit, OffsetDateTime lastModifiedAt, UUID lastModifiedByUserId, Long version, Set<ShoppingCartItemPersistenceEntity> items) {
		this.id = id;
		this.customer = customer;
		this.totalAmount = totalAmount;
		this.totalItems = totalItems;
		this.createdAt = createdAt;
		this.createdByUserId = createdByUserId;
		this.createdAtAudit = createdAtAudit;
		this.lastModifiedAt = lastModifiedAt;
		this.lastModifiedByUserId = lastModifiedByUserId;
		this.version = version;
		replaceItems(items);
	}

	public void replaceItems(Set<ShoppingCartItemPersistenceEntity> items) {
		if (items == null || items.isEmpty()) {
			setItems(new HashSet<>());
			return;
		}

		items.forEach(i -> i.setShoppingCart(this));
		setItems(items);
	}

	public void addItem(@NonNull ShoppingCartItemPersistenceEntity item) {
		item.setShoppingCart(this);

		if (getItems() == null) setItems(new HashSet<>());

		getItems().add(item);
	}

	public UUID getCustomerId() {
		return getCustomer().getId();
	}
}
