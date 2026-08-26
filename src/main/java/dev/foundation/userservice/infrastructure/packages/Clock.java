package dev.foundation.userservice.infrastructure.packages;

import dev.foundation.userservice.application.ports.ClockPort;
import dev.foundation.userservice.domain.value_objects.Date;
import dev.foundation.userservice.errors.ValueObjectException;

import java.time.Instant;

public class Clock implements ClockPort {
    @Override
    public Date nowIso8601() throws ValueObjectException {
        return Date.create(Instant.now().toString());
    }
}
