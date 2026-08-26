package dev.foundation.userservice.application.ports;

import java.util.UUID;

public interface IdGeneratorPort {
    UUID generate();
}
