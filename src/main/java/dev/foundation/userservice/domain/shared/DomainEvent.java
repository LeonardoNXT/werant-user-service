package dev.foundation.userservice.domain.shared;
import dev.foundation.userservice.domain.value_objects.Date;
import java.util.UUID;

public class DomainEvent {
    private final UUID eventId;
    private final UUID aggregateId;
    private final String eventType;
    private final String aggregateType;
    private final String reason;
    private final Date occurredAt;

    public DomainEvent(UUID eventId, UUID aggregateId, String eventType, String aggregateType, String reason, Date occurredAt){
        this.eventId = eventId;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.aggregateType = aggregateType;
        this.reason = reason;
        this.occurredAt =  occurredAt;
    }

    public UUID eventId(){
        return this.eventId;
    }

    public UUID aggregateId(){
        return this.aggregateId;
    }

    public String eventType(){
        return this.eventType;
    }

    public String aggregateType(){
        return this.aggregateType;
    }

    public String reason(){
        return this.reason;
    }

    public Date occurredAt(){
        return this.occurredAt;
    }
}