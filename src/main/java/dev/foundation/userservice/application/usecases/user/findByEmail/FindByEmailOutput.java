package dev.foundation.userservice.application.usecases.user.findByEmail;

import java.time.Instant;

public record FindByEmailOutput(
		String name,
		String username,
		String email,
		String profilePictureUrl,
		String status,
		boolean emailVerified,
		boolean numberVerified,
		Instant createdAt,
		Instant updatedAt
) {
}
