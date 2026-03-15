package com.lutz.algashop.ordering.infrastructure.persistence.shoppingCart;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString(of = "id")
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class ShoppingCartItemPersistenceEntity {
	@Id
	@EqualsAndHashCode.Include
	private UUID id;

	@JoinColumn
	@ManyToOne(optional = false)
	private ShoppingCartPersistenceEntity shoppingCart;

	private UUID productId;
	private String productName;
	private BigDecimal price;
	private Integer quantity;
	private BigDecimal totalAmount;
	private Boolean available;

	@CreatedBy
	private UUID createdByUserId;
	@CreatedDate
	private OffsetDateTime createdAt;
	@LastModifiedDate
	private OffsetDateTime lastModifiedAt;
	@LastModifiedBy
	private UUID lastModifiedByUserId;

	@Version
	private Long version;

	@Builder
	public ShoppingCartItemPersistenceEntity(UUID id, ShoppingCartPersistenceEntity shoppingCart, UUID productId, String productName, BigDecimal price, Integer quantity, BigDecimal totalAmount, Boolean available, UUID createdByUserId, OffsetDateTime createdAt, OffsetDateTime lastModifiedAt, UUID lastModifiedByUserId, Long version) {
		this.id = id;
		this.shoppingCart = shoppingCart;
		this.productId = productId;
		this.productName = productName;
		this.price = price;
		this.quantity = quantity;
		this.totalAmount = totalAmount;
		this.available = available;
		this.createdByUserId = createdByUserId;
		this.createdAt = createdAt;
		this.lastModifiedAt = lastModifiedAt;
		this.lastModifiedByUserId = lastModifiedByUserId;
		this.version = version;
	}

	public UUID getShoppingCartId() {
		if (getShoppingCart() == null) return null;
		return getShoppingCart().getId();
	}
}
