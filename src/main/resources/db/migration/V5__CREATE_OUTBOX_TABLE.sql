CREATE TABLE outbox
(
    id                    UUID PRIMARY KEY,
    aggregate_id          UUID                     NOT NULL,
    aggregate_type        VARCHAR(255)             NOT NULL,
    event_type            VARCHAR(255)             NOT NULL,
    reason                TEXT,
    occurred_at           TIMESTAMP WITH TIME ZONE NOT NULL,
    retry_limit           INT                      NOT NULL DEFAULT 5,
    retry_count           INT                      NOT NULL DEFAULT 0,
    status                VARCHAR(50)              NOT NULL DEFAULT 'pending',
    processing_started_at TIMESTAMP WITH TIME ZONE,
    last_attempt_at       TIMESTAMP WITH TIME ZONE,
    next_attempt_at       TIMESTAMP WITH TIME ZONE,
    published_at          TIMESTAMP WITH TIME ZONE,
    created_at            TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at            TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT chk_outbox_retry_count
        CHECK (retry_count >= 0),

    CONSTRAINT chk_outbox_retry_limit
        CHECK (retry_limit >= 0),

    CONSTRAINT chk_outbox_status
        CHECK (
            status IN (
                       'pending',
                       'processing',
                       'published',
                       'failed'
                )
            )
);

CREATE INDEX idx_outbox_processing
    ON outbox (status, next_attempt_at, created_at);

CREATE INDEX idx_outbox_aggregate
    ON outbox (aggregate_type, aggregate_id);