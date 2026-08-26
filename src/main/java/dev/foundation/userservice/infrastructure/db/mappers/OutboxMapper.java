package dev.foundation.userservice.infrastructure.db.mappers;

import dev.foundation.userservice.domain.entities.Outbox;
import dev.foundation.userservice.infrastructure.db.entities.OutboxEntity;

public class OutboxMapper {
    public static OutboxEntity toOrm(Outbox outbox){
        return OutboxEntity.builder().id(outbox.eventId()).aggregateId(outbox.aggregateId()).eventType(outbox.eventType()).aggregateType(outbox.aggregateType()).reason(outbox.reason()).occurredAt(outbox.occurredAt().value()).retryLimit(outbox.retryLimit()).retryCount(outbox.retryCount()).status(outbox.status()).processingStartedAt(outbox.processingStartedAt() != null ? outbox.processingStartedAt().value() : null).lastAttemptAt(outbox.lastAttemptAt() != null ? outbox.lastAttemptAt().value() : null).publishedAt(outbox.publishedAt() != null ? outbox.publishedAt().value() : null).createdAt(outbox.createdAt().value()).updated_at(outbox.updatedAt().value()).build();
    }

    // Outbox never going to process in this service, and then, does not need any go back to domain.
}
