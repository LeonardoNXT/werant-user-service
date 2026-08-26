package dev.foundation.userservice.domain.events;

import dev.foundation.userservice.domain.enums.AggregateTypeEnum;
import dev.foundation.userservice.domain.enums.EventsEnum;
import dev.foundation.userservice.domain.shared.DomainEvent;
import dev.foundation.userservice.domain.value_objects.Date;

import java.util.UUID;

public class ActivateUserEvent extends DomainEvent {
    private final static String eventType = EventsEnum.USER_EVENTS_ENUM.ACTIVATE.toString().toLowerCase();
    private final static  String aggregateType = AggregateTypeEnum.USER.toString().toLowerCase();
    private ActivateUserEvent(UUID eventId, String reason, UUID aggregateId, Date occurredAt){
        super(eventId, aggregateId, eventType, aggregateType, reason, occurredAt);
    }

    public static ActivateUserEvent create(UUID eventId, String reason, UUID aggregateId, Date occurredAt){
        return new ActivateUserEvent(eventId,reason, aggregateId,occurredAt);
    }
}
