package com.lutz.algashop.ordering.infrastructure.persistence.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderItemPersistenceEntity {
	@Id
	@EqualsAndHashCode.Include
	private Long id;

	@JoinColumn
	@ManyToOne(optional = false)
	private OrderPersistenceEntity order;

	private UUID productId;
	private String productName;
	private BigDecimal price;
	private Integer quantity;
	private BigDecimal totalAmount;

	public Long getOrderId() {
		if (getOrder() == null) return null;
		return getOrder().getId();
	}
}
