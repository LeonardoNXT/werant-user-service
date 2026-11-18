package dev.foundation.userservice.application.ports;

import dev.foundation.userservice.domain.value_objects.Link;

public interface ImagePresignUrlGeneratorPort {
	Link generate(String key);
}
