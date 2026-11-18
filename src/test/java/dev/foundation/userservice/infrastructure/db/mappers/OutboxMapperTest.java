package dev.foundation.userservice.infrastructure.db.mappers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.foundation.userservice.domain.entities.Outbox;
import dev.foundation.userservice.domain.shared.DomainEvent;
import dev.foundation.userservice.domain.value_objects.Date;
import dev.foundation.userservice.infrastructure.db.entities.OutboxEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class OutboxMapperTest {
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Test
	void shouldSerializeDomainEventAsJsonPayload() throws Exception {
		UUID eventId = UUID.randomUUID();
		UUID aggregateId = UUID.randomUUID();
		Date occurredAt = Date.create("2026-12-01T00:00:00Z");
		DomainEvent event = new DomainEvent(
				eventId,
				aggregateId,
				"create",
				"user",
				null,
				occurredAt
		);

		OutboxEntity entity = OutboxMapper.toOrm(
				Outbox.create(event, Date.create("2026-12-01T00:00:01Z"))
		);
		JsonNode payload = OBJECT_MAPPER.readTree(entity.getPayload());

		assertFalse(payload.isEmpty());
		assertEquals(eventId.toString(), payload.get("eventId").asText());
		assertEquals(aggregateId.toString(), payload.get("aggregateId").asText());
		assertEquals("create", payload.get("eventType").asText());
		assertEquals("user", payload.get("aggregateType").asText());
		assertEquals(occurredAt.value().toString(), payload.get("occurredAt").asText());
	}
}
