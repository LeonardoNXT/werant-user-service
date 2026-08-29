package dev.foundation.userservice.application.ports;

import dev.foundation.userservice.domain.entities.User;
import dev.foundation.userservice.errors.InfrastructureException;
import dev.foundation.userservice.errors.SystemException;
import dev.foundation.userservice.errors.ValueObjectException;

import java.util.List;
import java.util.UUID;

public interface UserQueryRepositoryPort {
	 User findById(UUID id) throws InfrastructureException, ValueObjectException;
	 User findByEmail(String email) throws SystemException;
	 List<User> findAll(int pageStart, int pageLimit) throws SystemException;
}
