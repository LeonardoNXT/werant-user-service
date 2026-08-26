package dev.foundation.userservice.domain.aggregate;

import dev.foundation.userservice.domain.shared.DomainEvent;

import java.util.ArrayList;
import java.util.List;

public class AggregateRoot {
    private final List<DomainEvent> domainEvents;

    protected AggregateRoot(){
        this.domainEvents = new ArrayList<DomainEvent>();
    }

    protected void addDomainEvents(DomainEvent domainEvent){
         this.domainEvents.add(domainEvent);
    }

    public void clearDomainEvents() {
        this.domainEvents.clear();
    }

    public List<DomainEvent> allDomainEvents() {
        return List.copyOf(this.domainEvents);
    }

}
