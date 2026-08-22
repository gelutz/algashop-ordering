package com.lutz.algashop.ordering.infrastructure.persistence.customer;

import com.lutz.algashop.ordering.application.customer.query.*;
import com.lutz.algashop.ordering.domain.customer.CustomerId;
import com.lutz.algashop.ordering.domain.customer.CustomerNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerQueryServiceImpl implements CustomerQueryService {
	private final EntityManager entityManager;

	private static final String findByIdAsOutputJPQL = """
            SELECT new com.lutz.algashop.ordering.application.customer.query.CustomerOutput(
                c.id,
                c.firstName,
                c.lastName,
                c.email,
                c.phone,
                c.document,
                c.birthdate,
                c.promotionNotificationAllowed,
                c.archived,
                c.loyaltyPoints,
                c.registeredAt,
                c.archivedAt,
                new com.lutz.algashop.ordering.application.commons.AddressData(
                    c.address.street,
                    c.address.number,
                    c.address.complement,
                    c.address.neighborhood,
                    c.address.city,
                    c.address.state,
                    c.address.zipCode
                )
            )
            FROM CustomerPersistenceEntity c
            WHERE c.id = :id""";

	@Override
	public CustomerOutput findById(UUID customerId) {
		try {
			TypedQuery<CustomerOutput> query = entityManager.createQuery(findByIdAsOutputJPQL, CustomerOutput.class);
			query.setParameter("id", customerId);

			return query.getSingleResult();
		} catch (NoResultException e) {
			throw new CustomerNotFoundException(new CustomerId(customerId));
		}
	}

	@Override
	public Page<CustomerSummaryOutput> filter(CustomerFilter filter) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<CustomerSummaryOutput> query = cb.createQuery(CustomerSummaryOutput.class);
		Root<CustomerPersistenceEntity> root = query.from(CustomerPersistenceEntity.class);

		query.select(cb.construct(CustomerSummaryOutput.class,
				root.get("id"),
				root.get("firstName"),
				root.get("lastName"),
				root.get("email"),
				root.get("document"),
				root.get("phone"),
				root.get("birthdate"),
				root.get("loyaltyPoints"),
				root.get("registeredAt"),
				root.get("archivedAt"),
				root.get("promotionNotificationAllowed"),
				root.get("archived")
		));
		query.where(buildPredicates(cb, root, filter));
		query.orderBy(buildOrder(cb, root, filter));

		TypedQuery<CustomerSummaryOutput> typedQuery = entityManager.createQuery(query);
		typedQuery.setFirstResult(filter.getPage() * filter.getSize());
		typedQuery.setMaxResults(filter.getSize());
		List<CustomerSummaryOutput> results = typedQuery.getResultList();

		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		Root<CustomerPersistenceEntity> countRoot = countQuery.from(CustomerPersistenceEntity.class);
		countQuery.select(cb.count(countRoot));
		countQuery.where(buildPredicates(cb, countRoot, filter));
		long total = entityManager.createQuery(countQuery).getSingleResult();

		return new PageImpl<>(results, PageRequest.of(filter.getPage(), filter.getSize()), total);
	}

	private Predicate[] buildPredicates(CriteriaBuilder cb,
	                                     Root<CustomerPersistenceEntity> root,
	                                     CustomerFilter filter) {
		List<Predicate> predicates = new ArrayList<>();
		if (filter.getFirstName() != null && !filter.getFirstName().isBlank()) {
			predicates.add(cb.like(cb.lower(root.get("firstName")),
					"%" + filter.getFirstName().toLowerCase() + "%"));
		}
		if (filter.getEmail() != null && !filter.getEmail().isBlank()) {
			predicates.add(cb.like(cb.lower(root.get("email")),
					"%" + filter.getEmail().toLowerCase() + "%"));
		}
		return predicates.toArray(new Predicate[0]);
	}

	private Order buildOrder(CriteriaBuilder cb,
	                          Root<CustomerPersistenceEntity> root,
	                          CustomerFilter filter) {
		String prop = filter.getSortByPropertyOrDefault().getPropertyName();
		return filter.getSortDirectionOrDefault() == Sort.Direction.ASC
				? cb.asc(root.get(prop))
				: cb.desc(root.get(prop));
	}
}
