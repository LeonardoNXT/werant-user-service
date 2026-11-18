package dev.foundation.userservice.infrastructure.db.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.foundation.userservice.domain.entities.Outbox;
import dev.foundation.userservice.infrastructure.db.entities.OutboxEntity;

public class OutboxMapper {
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static OutboxEntity toOrm(Outbox outbox){
		return OutboxEntity.builder()
				.id(outbox.eventId())
				.aggregateId(outbox.aggregateId())
				.eventType(outbox.eventType())
				.aggregateType(outbox.aggregateType())
				.reason(outbox.reason())
				.payload(payload(outbox))
				.occurredAt(outbox.occurredAt().value())
				.retryLimit(outbox.retryLimit())
				.retryCount(outbox.retryCount())
				.status(outbox.status())
				.processingStartedAt(outbox.processingStartedAt() != null ? outbox.processingStartedAt().value() : null)
				.lastAttemptAt(outbox.lastAttemptAt() != null ? outbox.lastAttemptAt().value() : null)
				.publishedAt(outbox.publishedAt() != null ? outbox.publishedAt().value() : null)
				.createdAt(outbox.createdAt().value())
				.updated_at(outbox.updatedAt().value())
				.build();
    }

	private static String payload(Outbox outbox) {
		try {
			return OBJECT_MAPPER.writeValueAsString(
					new OutboxPayload(
							outbox.eventId().toString(),
							outbox.aggregateId().toString(),
							outbox.eventType(),
							outbox.aggregateType(),
							outbox.reason(),
							outbox.occurredAt().value().toString()
					)
			);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("Unable to serialize outbox payload.", e);
		}
	}

	private record OutboxPayload(
			String eventId,
			String aggregateId,
			String eventType,
			String aggregateType,
			String reason,
			String occurredAt
	) {}

    // Outbox never going to process in this service, and then, does not need any go back to domain.
}
