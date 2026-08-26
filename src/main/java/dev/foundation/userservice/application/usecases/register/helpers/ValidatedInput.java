package dev.foundation.userservice.application.usecases.register.helpers;

import dev.foundation.userservice.domain.value_objects.*;

import java.util.Optional;

public record ValidatedInput(
        Name name,
        Username username,
        Email email,
		PhoneNumber phoneNumber,
        Password password,
        Role role
) {
}
