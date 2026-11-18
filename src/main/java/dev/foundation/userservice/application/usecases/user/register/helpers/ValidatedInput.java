package dev.foundation.userservice.application.usecases.user.register.helpers;

import dev.foundation.userservice.domain.value_objects.*;

public record ValidatedInput(
        Name name,
        Username username,
        Email email,
		PhoneNumber phoneNumber,
        Password password
) {
}
