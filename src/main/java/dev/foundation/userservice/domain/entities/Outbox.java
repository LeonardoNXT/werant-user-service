package dev.foundation.userservice.domain.entities;

import dev.foundation.userservice.domain.shared.DomainEvent;
import dev.foundation.userservice.domain.value_objects.Date;

public class Outbox extends DomainEvent {
    private final int retryLimit = 5;
    private final int retryCount = 0;
    private final String status = "pending";
    private final Date processingStartedAt = null;
    private final Date lastAttemptAt = null;
    private final Date nextAttemptAt = null;
    private final Date publishedAt = null;
    private final Date createdAt;
    private final Date updatedAt;

    private Outbox(DomainEvent domainEvent, Date date){
        super(domainEvent.eventId(),domainEvent.aggregateId(),domainEvent.eventType(),domainEvent.aggregateType(), domainEvent.reason(), domainEvent.occurredAt());
        this.createdAt = date;
        this.updatedAt = date;
    }

    public static Outbox create(DomainEvent domainEvent, Date date){
        return new Outbox(domainEvent, date);
    }

    public int retryLimit(){
        return this.retryLimit;
    }

    public int retryCount(){
        return this.retryCount;
    }

    public String status(){
        return this.status;
    }

    public Date processingStartedAt(){
        return this.processingStartedAt;
    }

    public Date lastAttemptAt(){
        return this.lastAttemptAt;
    }

    public Date nextAttemptAt(){
        return this.nextAttemptAt;
    }

    public Date publishedAt(){
        return this.publishedAt;
    }

    public Date createdAt(){
        return this.createdAt;
    }

    public Date updatedAt(){
        return this.updatedAt;
    }
}
