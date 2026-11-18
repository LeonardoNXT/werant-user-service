package dev.foundation.userservice.application.usecases.user.findByEmail.helpers;

import dev.foundation.userservice.domain.entities.User;

public record UserAndProfilePictureKey(
		User user,
		String Key
) {
}
