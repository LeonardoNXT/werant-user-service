package dev.foundation.userservice.application.ports;

import dev.foundation.userservice.domain.value_objects.Date;
import dev.foundation.userservice.errors.ValueObjectException;

public interface ClockPort {
    Date nowIso8601() throws ValueObjectException;
}
