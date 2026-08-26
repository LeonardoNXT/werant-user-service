package dev.foundation.userservice.application.ports;

import dev.foundation.userservice.domain.value_objects.HashedPassword;
import dev.foundation.userservice.domain.value_objects.Password;

public interface PasswordHasherPort {
    HashedPassword hash(Password password);
	boolean verify(HashedPassword hashedPassword);
}
