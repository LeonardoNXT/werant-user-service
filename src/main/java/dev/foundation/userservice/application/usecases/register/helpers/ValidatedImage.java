package dev.foundation.userservice.application.usecases.register.helpers;

import dev.foundation.userservice.domain.value_objects.ProfilePictureHeight;
import dev.foundation.userservice.domain.value_objects.ProfilePictureKey;
import dev.foundation.userservice.domain.value_objects.ProfilePictureType;
import dev.foundation.userservice.domain.value_objects.ProfilePictureWidth;

public record ValidatedImage(
        ProfilePictureKey profilePictureKey,
        ProfilePictureType profilePictureType,
        ProfilePictureHeight profilePictureHeight,
        ProfilePictureWidth profilePictureWidth,
        float ratio
) { }
