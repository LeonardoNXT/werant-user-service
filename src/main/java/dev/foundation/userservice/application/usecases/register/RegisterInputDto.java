package dev.foundation.userservice.application.usecases.register;

import dev.foundation.userservice.domain.value_objects.Link;

import java.util.Optional;

public record RegisterInputDto(
		String name,
		String username,
		String email,
		Optional<String> phoneNumber,
		String password,
		boolean emailVerified,
		boolean numberVerified
) {

}
