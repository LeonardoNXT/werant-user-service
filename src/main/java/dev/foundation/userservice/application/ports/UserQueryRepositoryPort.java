package dev.foundation.userservice.application.ports;

import dev.foundation.userservice.application.usecases.user.findByEmail.helpers.UserAndProfilePictureKey;
import dev.foundation.userservice.domain.value_objects.Email;
import dev.foundation.userservice.errors.InfrastructureException;
import dev.foundation.userservice.errors.SystemException;
import dev.foundation.userservice.errors.ValueObjectException;

import java.util.List;
import java.util.UUID;

public interface UserQueryRepositoryPort {
	 UserAndProfilePictureKey findById(UUID id) throws InfrastructureException, ValueObjectException;
	 UserAndProfilePictureKey findByEmail(Email email) throws SystemException;
	 List<UserAndProfilePictureKey> findAll(int pageStart, int pageLimit) throws SystemException;
}
