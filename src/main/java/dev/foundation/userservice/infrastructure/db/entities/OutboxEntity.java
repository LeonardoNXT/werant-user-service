package dev.foundation.userservice.infrastructure.db.entities;

import dev.foundation.userservice.domain.value_objects.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="outbox")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEntity {
    @Id
    private UUID id;

    @Column(name = "aggregate_id", nullable = false)
    private UUID aggregateId;

    @Column(name="event_type", nullable = false)
    private String eventType;

    @Column(name = "aggregate_type", nullable = false)
    private String aggregateType;

    @Column(name = "reason")
    private String reason;

	@Column(name = "payload", columnDefinition = "jsonb", nullable = false)
	private String payload;

    @Column(name="occurred_at", nullable = false)
    private Instant occurredAt;

    @Column(name="retry_limit", nullable = false)
    private int retryLimit;

    @Column(name="retry_count", nullable = false)
    private int retryCount;

    @Column(name="status",nullable = false)
    private String status;

    @Column(name="processing_started_at", nullable = true)
    private Instant processingStartedAt;

    @Column(name="last_attempt_at", nullable = true)
    private Instant lastAttemptAt;

    @Column(name="published_at", nullable = true)
    private Instant publishedAt;

    @Column(name="created_at", nullable = false)
    private Instant createdAt;

    @Column(name="updated_at", nullable = false)
    private Instant updated_at;
}
