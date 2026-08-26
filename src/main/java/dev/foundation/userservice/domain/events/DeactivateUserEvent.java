package dev.foundation.userservice.domain.events;

import dev.foundation.userservice.domain.enums.AggregateTypeEnum;
import dev.foundation.userservice.domain.enums.EventsEnum;
import dev.foundation.userservice.domain.shared.DomainEvent;
import dev.foundation.userservice.domain.value_objects.Date;

import java.util.UUID;

public class DeactivateUserEvent extends DomainEvent {
    private final static String eventType = EventsEnum.USER_EVENTS_ENUM.DEACTIVATE.toString().toLowerCase();
    private final static  String aggregateType = AggregateTypeEnum.USER.toString().toLowerCase();
    private DeactivateUserEvent(UUID eventId, String reason, UUID aggregateId, Date occurredAt){
        super(eventId, aggregateId, eventType, aggregateType, reason, occurredAt);
    }

    public static DeactivateUserEvent create(UUID eventId,String reason, UUID aggregateId, Date occurredAt){
        return new DeactivateUserEvent(eventId,reason, aggregateId,occurredAt);
    }
}
