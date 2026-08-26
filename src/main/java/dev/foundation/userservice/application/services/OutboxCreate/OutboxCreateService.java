package dev.foundation.userservice.application.services.OutboxCreate;

import dev.foundation.userservice.domain.entities.Outbox;
import dev.foundation.userservice.domain.shared.DomainEvent;
import dev.foundation.userservice.domain.value_objects.Date;

import java.util.List;
import java.util.Optional;

public class OutboxCreateService {
    public static List<Outbox> execute(List<DomainEvent> domainEvents, Date date){
        return domainEvents.stream().map(event -> Outbox.create(event, date)).toList();
    }
}
