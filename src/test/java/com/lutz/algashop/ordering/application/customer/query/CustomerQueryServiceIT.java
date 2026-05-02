package com.lutz.algashop.ordering.application.customer.query;

import com.lutz.algashop.ordering.application.customer.management.CustomerManagementApplicationService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

import static com.lutz.algashop.ordering.application.customer.management.builder.CustomerInputTestBuilder.aCustomerInput;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CustomerQueryServiceIT {

	@Autowired
	private CustomerQueryService sut;

	@Autowired
	private CustomerManagementApplicationService managementService;

	@Autowired
	private EntityManager entityManager;

	@Test
	void shouldFilterByFirstName() {
		managementService.create(aCustomerInput().withFirstName("Alice")
		                                         .withDocument("111")
		                                         .withEmail("alice@test.com")
		                                         .build());
		managementService.create(aCustomerInput().withFirstName("Bob")
		                                         .withDocument("222")
		                                         .withEmail("bob@test.com")
		                                         .build());

		CustomerFilter filter = new CustomerFilter();
		filter.setFirstName("ali");

		Page<CustomerSummaryOutput> result = sut.filter(filter);

		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent().getFirst().getFirstName()).isEqualTo("Alice");
	}

	@Test
	void shouldFilterByFirstNameCaseInsensitive() {
		managementService.create(aCustomerInput().withFirstName("Alice")
		                                         .withDocument("111")
		                                         .withEmail("alice@test.com")
		                                         .build());
		managementService.create(aCustomerInput().withFirstName("Bob")
		                                         .withDocument("222")
		                                         .withEmail("bob@test.com")
		                                         .build());

		CustomerFilter filter = new CustomerFilter();
		filter.setFirstName("ALICE");

		Page<CustomerSummaryOutput> result = sut.filter(filter);

		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent().getFirst().getFirstName()).isEqualTo("Alice");
	}

	@Test
	void shouldFilterByEmail() {
		managementService.create(aCustomerInput().withDocument("111").withEmail("john@mail.com").build());
		managementService.create(aCustomerInput().withDocument("222").withEmail("jane@mail.com").build());

		CustomerFilter filter = new CustomerFilter();
		filter.setEmail("john");

		Page<CustomerSummaryOutput> result = sut.filter(filter);

		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent().getFirst().getEmail()).isEqualTo("john@mail.com");
	}

	@Test
	void shouldFilterByEmailCaseInsensitive() {
		managementService.create(aCustomerInput().withDocument("111").withEmail("john@mail.com").build());
		managementService.create(aCustomerInput().withDocument("222").withEmail("jane@mail.com").build());

		CustomerFilter filter = new CustomerFilter();
		filter.setEmail("JOHN");

		Page<CustomerSummaryOutput> result = sut.filter(filter);

		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent().getFirst().getEmail()).isEqualTo("john@mail.com");
	}

	@Test
	void shouldFilterByFirstNameAndEmailAsAnd() {
		managementService.create(aCustomerInput().withFirstName("Alice")
		                                         .withDocument("111")
		                                         .withEmail("alice.john@mail.com")
		                                         .build());
		managementService.create(aCustomerInput().withFirstName("Alice")
		                                         .withDocument("222")
		                                         .withEmail("alice.jane@mail.com")
		                                         .build());

		CustomerFilter filter = new CustomerFilter();
		filter.setFirstName("alice");
		filter.setEmail("jane");

		Page<CustomerSummaryOutput> result = sut.filter(filter);

		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent().getFirst().getEmail()).isEqualTo("alice.jane@mail.com");
	}

	@Test
	void shouldPaginate() {
		for (int i = 1; i <= 5; i++) {
			managementService.create(aCustomerInput()
					                         .withDocument(String.valueOf(i))
					                         .withEmail("customer" + i + "@test.com")
					                         .build());
		}

		CustomerFilter page0 = new CustomerFilter(2, 0);
		CustomerFilter page1 = new CustomerFilter(2, 1);
		CustomerFilter page2 = new CustomerFilter(2, 2);

		Page<CustomerSummaryOutput> result0 = sut.filter(page0);
		Page<CustomerSummaryOutput> result1 = sut.filter(page1);
		Page<CustomerSummaryOutput> result2 = sut.filter(page2);

		assertThat(result0.getContent()).hasSize(2);
		assertThat(result1.getContent()).hasSize(2);
		assertThat(result2.getContent()).hasSize(1);
		assertThat(result0.getTotalElements()).isEqualTo(5);
	}

	@Test
	void shouldSortByFirstNameAsc() {
		managementService.create(aCustomerInput().withFirstName("Charlie")
		                                         .withDocument("111")
		                                         .withEmail("charlie@test.com")
		                                         .build());
		managementService.create(aCustomerInput().withFirstName("Alice")
		                                         .withDocument("222")
		                                         .withEmail("alice@test.com")
		                                         .build());
		managementService.create(aCustomerInput().withFirstName("Bob")
		                                         .withDocument("333")
		                                         .withEmail("bob@test.com")
		                                         .build());

		CustomerFilter filter = new CustomerFilter();
		filter.setSortByProperty(CustomerFilter.SortType.FIRST_NAME);
		filter.setSortDirection(Sort.Direction.ASC);

		Page<CustomerSummaryOutput> result = sut.filter(filter);

		assertThat(result.getContent()).extracting(CustomerSummaryOutput::getFirstName)
		                               .containsExactly("Alice", "Bob", "Charlie");
	}

	@Test
	void shouldSortByFirstNameDesc() {
		managementService.create(aCustomerInput().withFirstName("Charlie")
		                                         .withDocument("111")
		                                         .withEmail("charlie@test.com")
		                                         .build());
		managementService.create(aCustomerInput().withFirstName("Alice")
		                                         .withDocument("222")
		                                         .withEmail("alice@test.com")
		                                         .build());
		managementService.create(aCustomerInput().withFirstName("Bob")
		                                         .withDocument("333")
		                                         .withEmail("bob@test.com")
		                                         .build());

		CustomerFilter filter = new CustomerFilter();
		filter.setSortByProperty(CustomerFilter.SortType.FIRST_NAME);
		filter.setSortDirection(Sort.Direction.DESC);

		Page<CustomerSummaryOutput> result = sut.filter(filter);

		assertThat(result.getContent()).extracting(CustomerSummaryOutput::getFirstName)
		                               .containsExactly("Charlie", "Bob", "Alice");
	}

	@Test
	void shouldSortByRegisteredAtAsc() {
		UUID firstId = managementService.create(aCustomerInput().withDocument("111")
		                                                        .withEmail("first@test.com")
		                                                        .build());
		UUID secondId = managementService.create(aCustomerInput().withDocument("222")
		                                                         .withEmail("second@test.com")
		                                                         .build());
		UUID thirdId = managementService.create(aCustomerInput().withDocument("333")
		                                                        .withEmail("third@test.com")
		                                                        .build());

		entityManager.flush();
		setRegisteredAt(firstId, OffsetDateTime.now().minusDays(2));
		setRegisteredAt(secondId, OffsetDateTime.now().minusDays(1));
		setRegisteredAt(thirdId, OffsetDateTime.now());
		entityManager.flush();
		entityManager.clear();

		CustomerFilter filter = new CustomerFilter();
		filter.setSortByProperty(CustomerFilter.SortType.REGISTERED_AT);
		filter.setSortDirection(Sort.Direction.ASC);

		Page<CustomerSummaryOutput> result = sut.filter(filter);

		assertThat(result.getContent()).extracting(CustomerSummaryOutput::getEmail)
		                               .containsExactly("first@test.com", "second@test.com", "third@test.com");
	}

	@Test
	void shouldSortByRegisteredAtDesc() {
		UUID firstId = managementService.create(aCustomerInput().withDocument("111")
		                                                        .withEmail("first@test.com")
		                                                        .build());
		UUID secondId = managementService.create(aCustomerInput().withDocument("222")
		                                                         .withEmail("second@test.com")
		                                                         .build());
		UUID thirdId = managementService.create(aCustomerInput().withDocument("333")
		                                                        .withEmail("third@test.com")
		                                                        .build());

		entityManager.flush();
		setRegisteredAt(firstId, OffsetDateTime.now().minusDays(2));
		setRegisteredAt(secondId, OffsetDateTime.now().minusDays(1));
		setRegisteredAt(thirdId, OffsetDateTime.now());
		entityManager.flush();
		entityManager.clear();

		CustomerFilter filter = new CustomerFilter();
		filter.setSortByProperty(CustomerFilter.SortType.REGISTERED_AT);
		filter.setSortDirection(Sort.Direction.DESC);

		Page<CustomerSummaryOutput> result = sut.filter(filter);

		assertThat(result.getContent()).extracting(CustomerSummaryOutput::getEmail)
		                               .containsExactly("third@test.com", "second@test.com", "first@test.com");
	}

	@Test
	void shouldReturnEmptyPageWhenNoMatch() {
		managementService.create(aCustomerInput().withFirstName("Alice")
		                                         .withDocument("111")
		                                         .withEmail("alice@test.com")
		                                         .build());

		CustomerFilter filter = new CustomerFilter();
		filter.setFirstName("zzznomatch");

		Page<CustomerSummaryOutput> result = sut.filter(filter);

		assertThat(result.getContent()).isEmpty();
		assertThat(result.getTotalElements()).isZero();
	}

	private void setRegisteredAt(UUID customerId, OffsetDateTime registeredAt) {
		entityManager.createQuery("UPDATE CustomerPersistenceEntity c SET c.registeredAt = :ts WHERE c.id = :id")
		             .setParameter("ts", registeredAt)
		             .setParameter("id", customerId)
		             .executeUpdate();
	}
}
