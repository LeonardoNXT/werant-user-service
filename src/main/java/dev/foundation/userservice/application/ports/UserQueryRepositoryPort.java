package dev.foundation.userservice.application.ports;

import dev.foundation.userservice.domain.entities.User;
import dev.foundation.userservice.errors.InfrastructureException;
import dev.foundation.userservice.errors.ValueObjectException;

import java.util.UUID;

public interface UserQueryRepositoryPort {
	 User findById(UUID id) throws InfrastructureException, ValueObjectException;
	 User findByEmail(UUID email);
	 User findAll(int pageStart, int pageLimit);
}
