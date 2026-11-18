package dev.foundation.userservice.infrastructure.db.projections;

import dev.foundation.userservice.infrastructure.db.entities.UserEntity;

public record UserAndProfilePictureKeyProjection(
		UserEntity userEntity,
		String key
) {
}
