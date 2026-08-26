package dev.foundation.userservice.domain.Usecases;

import dev.foundation.userservice.domain.entities.Outbox;
import dev.foundation.userservice.domain.events.CreateUserEvent;
import dev.foundation.userservice.domain.shared.DomainEvent;
import dev.foundation.userservice.domain.value_objects.Date;
import dev.foundation.userservice.errors.ValueObjectException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class OutboxTest {

	private static final String OCCURRED_AT = "2026-12-01T00:00:00Z";
	private static final String CREATED_AT = "2026-12-02T00:00:00Z";

	private DomainEvent createDomainEvent(
			UUID eventId,
			UUID aggregateId,
			String eventType,
			String aggregateType,
			String reason,
			Date occurredAt
	) {
		return new DomainEvent(
				eventId,
				aggregateId,
				eventType,
				aggregateType,
				reason,
				occurredAt
		);
	}

	private Outbox createOutbox() throws ValueObjectException {
		UUID eventId = UUID.randomUUID();
		UUID aggregateId = UUID.randomUUID();
		Date occurredAt = Date.create(OCCURRED_AT);
		Date createdAt = Date.create(CREATED_AT);

		DomainEvent event = createDomainEvent(
				eventId,
				aggregateId,
				"user.created",
				"User",
				"User registration",
				occurredAt
		);

		return Outbox.create(event, createdAt);
	}

	@Test
	void shouldCreateOutbox() throws ValueObjectException {
		Outbox outbox = createOutbox();

		assertNotNull(outbox);
	}

	@Test
	void shouldPreserveDomainEventId() throws ValueObjectException {
		UUID eventId = UUID.randomUUID();

		DomainEvent event = createDomainEvent(
				eventId,
				UUID.randomUUID(),
				"user.created",
				"User",
				"User registration",
				Date.create(OCCURRED_AT)
		);

		Outbox outbox = Outbox.create(
				event,
				Date.create(CREATED_AT)
		);

		assertEquals(eventId, outbox.eventId());
	}

	@Test
	void shouldPreserveAggregateId() throws ValueObjectException {
		UUID aggregateId = UUID.randomUUID();

		DomainEvent event = createDomainEvent(
				UUID.randomUUID(),
				aggregateId,
				"user.created",
				"User",
				"User registration",
				Date.create(OCCURRED_AT)
		);

		Outbox outbox = Outbox.create(
				event,
				Date.create(CREATED_AT)
		);

		assertEquals(aggregateId, outbox.aggregateId());
	}

	@Test
	void shouldPreserveEventType() throws ValueObjectException {
		DomainEvent event = createDomainEvent(
				UUID.randomUUID(),
				UUID.randomUUID(),
				"user.created",
				"User",
				"User registration",
				Date.create(OCCURRED_AT)
		);

		Outbox outbox = Outbox.create(
				event,
				Date.create(CREATED_AT)
		);

		assertEquals("user.created", outbox.eventType());
	}

	@Test
	void shouldPreserveAggregateType() throws ValueObjectException {
		DomainEvent event = createDomainEvent(
				UUID.randomUUID(),
				UUID.randomUUID(),
				"user.created",
				"User",
				"User registration",
				Date.create(OCCURRED_AT)
		);

		Outbox outbox = Outbox.create(
				event,
				Date.create(CREATED_AT)
		);

		assertEquals("User", outbox.aggregateType());
	}

	@Test
	void shouldPreserveReason() throws ValueObjectException {
		DomainEvent event = createDomainEvent(
				UUID.randomUUID(),
				UUID.randomUUID(),
				"user.created",
				"User",
				"User registration",
				Date.create(OCCURRED_AT)
		);

		Outbox outbox = Outbox.create(
				event,
				Date.create(CREATED_AT)
		);

		assertEquals("User registration", outbox.reason());
	}

	@Test
	void shouldPreserveOccurredAt() throws ValueObjectException {
		Date occurredAt = Date.create(OCCURRED_AT);

		DomainEvent event = createDomainEvent(
				UUID.randomUUID(),
				UUID.randomUUID(),
				"user.created",
				"User",
				"User registration",
				occurredAt
		);

		Outbox outbox = Outbox.create(
				event,
				Date.create(CREATED_AT)
		);

		assertEquals(occurredAt, outbox.occurredAt());
	}

	@Test
	void shouldSetCreatedAt() throws ValueObjectException {
		Date createdAt = Date.create(CREATED_AT);

		DomainEvent event = createDomainEvent(
				UUID.randomUUID(),
				UUID.randomUUID(),
				"user.created",
				"User",
				"User registration",
				Date.create(OCCURRED_AT)
		);

		Outbox outbox = Outbox.create(event, createdAt);

		assertEquals(createdAt, outbox.createdAt());
	}

	@Test
	void shouldSetUpdatedAtToCreatedAt() throws ValueObjectException {
		Date createdAt = Date.create(CREATED_AT);

		DomainEvent event = createDomainEvent(
				UUID.randomUUID(),
				UUID.randomUUID(),
				"user.created",
				"User",
				"User registration",
				Date.create(OCCURRED_AT)
		);

		Outbox outbox = Outbox.create(event, createdAt);

		assertEquals(createdAt, outbox.updatedAt());
	}

	@Test
	void shouldStartWithDefaultRetryLimit() throws ValueObjectException {
		Outbox outbox = createOutbox();

		assertEquals(5, outbox.retryLimit());
	}

	@Test
	void shouldStartWithZeroRetryCount() throws ValueObjectException {
		Outbox outbox = createOutbox();

		assertEquals(0, outbox.retryCount());
	}

	@Test
	void shouldStartWithPendingStatus() throws ValueObjectException {
		Outbox outbox = createOutbox();

		assertEquals("pending", outbox.status());
	}

	@Test
	void shouldStartWithoutProcessingStartedAt() throws ValueObjectException {
		Outbox outbox = createOutbox();

		assertNull(outbox.processingStartedAt());
	}

	@Test
	void shouldStartWithoutLastAttemptAt() throws ValueObjectException {
		Outbox outbox = createOutbox();

		assertNull(outbox.lastAttemptAt());
	}

	@Test
	void shouldStartWithoutNextAttemptAt() throws ValueObjectException {
		Outbox outbox = createOutbox();

		assertNull(outbox.nextAttemptAt());
	}

	@Test
	void shouldStartWithoutPublishedAt() throws ValueObjectException {
		Outbox outbox = createOutbox();

		assertNull(outbox.publishedAt());
	}

	@Test
	void shouldExtendDomainEvent() throws ValueObjectException {
		Outbox outbox = createOutbox();

		assertInstanceOf(DomainEvent.class, outbox);
	}

	@Test
	void shouldPreserveAllDomainEventData() throws ValueObjectException {
		UUID eventId = UUID.randomUUID();
		UUID aggregateId = UUID.randomUUID();
		Date occurredAt = Date.create(OCCURRED_AT);
		Date createdAt = Date.create(CREATED_AT);

		DomainEvent event = createDomainEvent(
				eventId,
				aggregateId,
				"user.created",
				"User",
				"User registration",
				occurredAt
		);

		Outbox outbox = Outbox.create(event, createdAt);

		assertEquals(eventId, outbox.eventId());
		assertEquals(aggregateId, outbox.aggregateId());
		assertEquals("user.created", outbox.eventType());
		assertEquals("User", outbox.aggregateType());
		assertEquals("User registration", outbox.reason());
		assertEquals(occurredAt, outbox.occurredAt());
	}

	@Test
	void shouldCreateOutboxFromCreateUserEvent() throws ValueObjectException {
		UUID eventId = UUID.randomUUID();
		UUID aggregateId = UUID.randomUUID();
		Date occurredAt = Date.create(OCCURRED_AT);

		CreateUserEvent event = CreateUserEvent.create(
				eventId,
				aggregateId,
				occurredAt
		);

		Outbox outbox = Outbox.create(
				event,
				Date.create(CREATED_AT)
		);

		assertEquals(event.eventId(), outbox.eventId());
		assertEquals(event.aggregateId(), outbox.aggregateId());
		assertEquals(event.eventType(), outbox.eventType());
		assertEquals(event.aggregateType(), outbox.aggregateType());
		assertEquals(event.reason(), outbox.reason());
		assertEquals(event.occurredAt(), outbox.occurredAt());
	}
}