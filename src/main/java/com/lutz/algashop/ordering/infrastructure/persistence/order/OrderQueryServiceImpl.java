package com.lutz.algashop.ordering.infrastructure.persistence.order;

import com.lutz.algashop.ordering.application.order.query.CustomerMinimalOutput;
import com.lutz.algashop.ordering.application.order.query.OrderFilter;
import com.lutz.algashop.ordering.application.order.query.OrderQueryService;
import com.lutz.algashop.ordering.application.order.query.detail.OrderDetailOutput;
import com.lutz.algashop.ordering.application.order.query.summary.OrderSummaryOutput;
import com.lutz.algashop.ordering.application.utility.Mapper;
import com.lutz.algashop.ordering.domain.order.OrderId;
import com.lutz.algashop.ordering.domain.order.exception.OrderNotFoundException;
import com.lutz.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryServiceImpl implements OrderQueryService {
	private final OrderPersistenceEntityRepository repository;
	private final Mapper mapper;
	private final EntityManager entityManager;

	@Override
	public OrderDetailOutput findById(String id) {
		OrderPersistenceEntity orderPersistenceEntity = repository.findById(new OrderId(id).value().toLong())
		                                                          .orElseThrow(OrderNotFoundException::new);
		return mapper.convert(orderPersistenceEntity, OrderDetailOutput.class);
	}

	@Override
	public Page<OrderSummaryOutput> filter(OrderFilter filter) {
		Long total = countTotalQueryResults(filter);
		if (total.equals(0L)) {
			PageRequest pageRequest = PageRequest.of(filter.getPage(), filter.getSize());
			return new PageImpl<>(new ArrayList<>(), pageRequest, total);
		}
		
		return filterQuery(filter, total);
	}

	private Page<OrderSummaryOutput> filterQuery(OrderFilter filter, Long total) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OrderSummaryOutput> query = builder.createQuery(OrderSummaryOutput.class);
		Root<OrderPersistenceEntity> root = query.from(OrderPersistenceEntity.class);

		Path<CustomerPersistenceEntity> customerPath = root.get("customer");

		query.select(
				builder.construct(
						OrderSummaryOutput.class,
						root.get("id"),
						root.get("totalItems"),
						root.get("totalAmount"),
						root.get("placedAt"),
						root.get("paidAt"),
						root.get("readyAt"),
						root.get("canceledAt"),
						root.get("status"),
						root.get("paymentMethod"),
						builder.construct(
								CustomerMinimalOutput.class,
								customerPath.get("id"),
								customerPath.get("firstName"),
								customerPath.get("lastName"),
								customerPath.get("email"),
								customerPath.get("document"),
								customerPath.get("phone")
						)
					)
		);

		Predicate[] predicates = toPredicate(builder, root, filter);
		query.where(predicates);

		Order sortOrder = toSortOrder(builder, root, filter);
		query.orderBy(sortOrder);

		TypedQuery<OrderSummaryOutput> typedQuery = entityManager.createQuery(query);
		typedQuery.setFirstResult(filter.getSize() * filter.getPage());
		typedQuery.setMaxResults(filter.getSize());

		PageRequest pageRequest = PageRequest.of(filter.getPage(), filter.getSize());

		return new PageImpl<>(typedQuery.getResultList(), pageRequest, total);
	}

	private Order toSortOrder(CriteriaBuilder builder, Root<OrderPersistenceEntity> root, OrderFilter filter) {
		if (filter.getSortDirectionOrDefault() == Sort.Direction.ASC)
			return builder.asc(root.get(filter.getSortByPropertyOrDefault().getPropertyName()));

		if (filter.getSortDirectionOrDefault() == Sort.Direction.DESC)
			return builder.desc(root.get(filter.getSortByPropertyOrDefault().getPropertyName()));

		return null;
	}

	private Long countTotalQueryResults(OrderFilter filter) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<OrderPersistenceEntity> root = query.from(OrderPersistenceEntity.class);

		Expression<Long> countExpression = builder.count(root);
		Predicate[] predicates = toPredicate(builder, root, filter);

		query.select(countExpression);
		query.where(predicates);

		TypedQuery<Long> typedQuery = entityManager.createQuery(query);

		return typedQuery.getSingleResult();
	}


	private Predicate[] toPredicate(CriteriaBuilder builder, Root<OrderPersistenceEntity> root, OrderFilter filter) {
		List<Predicate> predicates = new ArrayList<>();

		if (filter.getCustomerId() != null) {
			predicates.add(builder.equal(root.get("customer").get("id"), filter.getCustomerId()));
		}
		if (filter.getOrderId() != null) {
			long orderIdAsLong;
			try {
				orderIdAsLong = new OrderId(filter.getOrderId()).value().toLong();
			} catch (IllegalArgumentException ignored) {
				orderIdAsLong = 0L;
			}
			predicates.add(builder.equal(root.get("id"), orderIdAsLong ));
		}
		if (StringUtils.hasText(filter.getStatus())) {
			predicates.add(builder.equal(root.get("status"), filter.getStatus().toUpperCase(Locale.ROOT)));
		}
		if (filter.getPlacedAtFrom() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("placedAt"), filter.getPlacedAtFrom()));
		}
		if (filter.getPlacedAtTo() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("placedAt"), filter.getPlacedAtTo()));
		}
		if (filter.getTotalAmountFrom() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("totalAmount"), filter.getTotalAmountFrom()));
		}
		if (filter.getTotalAmountTo() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("totalAmount"), filter.getTotalAmountTo()));
		}

		return predicates.toArray(new Predicate[]{});
	}
}
