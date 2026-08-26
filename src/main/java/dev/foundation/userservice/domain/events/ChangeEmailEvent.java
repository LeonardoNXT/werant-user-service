package dev.foundation.userservice.domain.events;

import dev.foundation.userservice.domain.enums.AggregateTypeEnum;
import dev.foundation.userservice.domain.enums.EventsEnum;
import dev.foundation.userservice.domain.shared.DomainEvent;
import dev.foundation.userservice.domain.value_objects.Date;

import java.util.UUID;

public class ChangeEmailEvent extends DomainEvent {
    private final static String eventType = EventsEnum.USER_EVENTS_ENUM.CHANGE_EMAIL.toString().toLowerCase();
    private final static  String aggregateType = AggregateTypeEnum.USER.toString().toLowerCase();
    private ChangeEmailEvent(UUID eventId, UUID aggregateId, Date occurredAt){
        super(eventId, aggregateId, eventType, aggregateType, null, occurredAt);
    }

    public static ChangeEmailEvent create(UUID eventId, UUID aggregateId, Date occurredAt){
        return new ChangeEmailEvent(eventId, aggregateId,occurredAt);
    }
}
