package dev.foundation.userservice.application.services;

import dev.foundation.userservice.application.services.OutboxCreate.OutboxCreateService;
import dev.foundation.userservice.domain.entities.Outbox;
import dev.foundation.userservice.domain.shared.DomainEvent;
import dev.foundation.userservice.domain.value_objects.Date;
import dev.foundation.userservice.errors.ValueObjectException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class OutboxCreateServiceTest {

	private static final Date DATE;

	static {
		try {
			DATE = Date.create("2026-12-01T00:00:00Z");
		} catch (ValueObjectException e) {
			throw new RuntimeException(e);
		}
	}

	private DomainEvent createEvent() throws ValueObjectException {
		return new DomainEvent(
				UUID.randomUUID(),
				UUID.randomUUID(),
				"user.created",
				"User",
				"User registration",
				Date.create("2026-12-01T00:00:00Z")
		);
	}

	@Test
	void shouldCreateOutboxFromDomainEvent() throws ValueObjectException {
		DomainEvent event = createEvent();

		List<Outbox> outboxes = OutboxCreateService.execute(
				List.of(event),
				DATE
		);

		assertEquals(1, outboxes.size());
		assertNotNull(outboxes.get(0));
	}

	@Test
	void shouldCreateOneOutboxForEachDomainEvent() throws ValueObjectException {
		DomainEvent firstEvent = createEvent();
		DomainEvent secondEvent = createEvent();
		DomainEvent thirdEvent = createEvent();

		List<Outbox> outboxes = OutboxCreateService.execute(
				List.of(firstEvent, secondEvent, thirdEvent),
				DATE
		);

		assertEquals(3, outboxes.size());
	}

	@Test
	void shouldPreserveDomainEventData() throws ValueObjectException {
		UUID eventId = UUID.randomUUID();
		UUID aggregateId = UUID.randomUUID();
		Date occurredAt = Date.create("2026-12-01T00:00:00Z");

		DomainEvent event = new DomainEvent(
				eventId,
				aggregateId,
				"user.created",
				"User",
				"User registration",
				occurredAt
		);

		List<Outbox> outboxes = OutboxCreateService.execute(
				List.of(event),
				DATE
		);

		Outbox outbox = outboxes.get(0);

		assertEquals(eventId, outbox.eventId());
		assertEquals(aggregateId, outbox.aggregateId());
		assertEquals("user.created", outbox.eventType());
		assertEquals("User", outbox.aggregateType());
		assertEquals("User registration", outbox.reason());
		assertEquals(occurredAt, outbox.occurredAt());
	}

	@Test
	void shouldUseProvidedDateForOutboxCreation() throws ValueObjectException {
		Date date = Date.create("2026-12-05T12:30:00Z");

		List<Outbox> outboxes = OutboxCreateService.execute(
				List.of(createEvent()),
				date
		);

		Outbox outbox = outboxes.get(0);

		assertEquals(date, outbox.createdAt());
		assertEquals(date, outbox.updatedAt());
	}

	@Test
	void shouldReturnEmptyListWhenNoDomainEventsAreProvided() throws ValueObjectException {
		List<Outbox> outboxes = OutboxCreateService.execute(
				List.of(),
				DATE
		);

		assertTrue(outboxes.isEmpty());
	}

	@Test
	void shouldPreserveDomainEventOrder() throws ValueObjectException {
		DomainEvent firstEvent = createEvent();
		DomainEvent secondEvent = createEvent();
		DomainEvent thirdEvent = createEvent();

		List<Outbox> outboxes = OutboxCreateService.execute(
				List.of(firstEvent, secondEvent, thirdEvent),
				DATE
		);

		assertEquals(firstEvent.eventId(), outboxes.get(0).eventId());
		assertEquals(secondEvent.eventId(), outboxes.get(1).eventId());
		assertEquals(thirdEvent.eventId(), outboxes.get(2).eventId());
	}

	@Test
	void shouldCreateOutboxesWithPendingStatus() throws ValueObjectException {
		List<Outbox> outboxes = OutboxCreateService.execute(
				List.of(createEvent(), createEvent()),
				DATE
		);

		assertEquals("pending", outboxes.get(0).status());
		assertEquals("pending", outboxes.get(1).status());
	}

	@Test
	void shouldCreateOutboxesWithZeroRetryCount() throws ValueObjectException {
		List<Outbox> outboxes = OutboxCreateService.execute(
				List.of(createEvent(), createEvent()),
				DATE
		);

		assertEquals(0, outboxes.get(0).retryCount());
		assertEquals(0, outboxes.get(1).retryCount());
	}

	@Test
	void shouldCreateOutboxesWithDefaultRetryLimit() throws ValueObjectException {
		List<Outbox> outboxes = OutboxCreateService.execute(
				List.of(createEvent(), createEvent()),
				DATE
		);

		assertEquals(5, outboxes.get(0).retryLimit());
		assertEquals(5, outboxes.get(1).retryLimit());
	}
}