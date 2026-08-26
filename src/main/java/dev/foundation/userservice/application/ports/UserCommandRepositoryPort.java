package dev.foundation.userservice.application.ports;

import dev.foundation.userservice.domain.entities.Outbox;
import dev.foundation.userservice.domain.entities.User;
import dev.foundation.userservice.errors.InfrastructureException;
import dev.foundation.userservice.errors.ValueObjectException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserCommandRepositoryPort {
    void persist(User user, List<Outbox> outboxes) throws InfrastructureException;
    User save(User user, List<Outbox> outboxes) throws InfrastructureException, ValueObjectException;
    void delete(UUID id, List<Outbox> outboxes) throws InfrastructureException;
}
