package dev.foundation.userservice.infrastructure.packages;

import dev.foundation.userservice.application.ports.IdGeneratorPort;

import java.util.UUID;

public class IdGenerator implements IdGeneratorPort {
    @Override
    public UUID generate() {
        return UUID.randomUUID();
    }
}
