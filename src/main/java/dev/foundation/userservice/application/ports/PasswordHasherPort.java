package dev.foundation.userservice.application.ports;

import dev.foundation.userservice.domain.value_objects.HashedPassword;
import dev.foundation.userservice.domain.value_objects.Password;
import dev.foundation.userservice.errors.SystemException;

public interface PasswordHasherPort {
    HashedPassword hash(Password password) throws SystemException;
	boolean verify(Password password,HashedPassword hashedPassword);
}
