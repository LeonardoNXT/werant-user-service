package dev.foundation.userservice.infrastructure.db.repositories.queries;

import dev.foundation.userservice.application.ports.UserQueryRepositoryPort;
import dev.foundation.userservice.domain.entities.User;
import dev.foundation.userservice.errors.InfrastructureException;
import dev.foundation.userservice.errors.ValueObjectException;
import dev.foundation.userservice.errors.types.ExceptionType;
import dev.foundation.userservice.infrastructure.db.entities.UserEntity;
import dev.foundation.userservice.infrastructure.db.mappers.UserMapper;
import dev.foundation.userservice.infrastructure.db.repositories.Repository;
import jakarta.persistence.EntityManager;

import java.util.UUID;

public class UserQueryRepository
		extends Repository {

	private UserQueryRepository(EntityManager entityManager) {
		super(entityManager);
	}

	public static UserQueryRepository create(EntityManager entityManager) {
		return new UserQueryRepository(entityManager);
	}


	public User findById(UUID id)
			throws InfrastructureException, ValueObjectException {

		try {

			UserEntity userEntity = entityManager
					.createQuery(
							"""
							SELECT u
							FROM UserEntity u
							WHERE u.id = :id
							""",
							UserEntity.class
					)
					.setParameter("id", id)
					.getSingleResultOrNull();

			if (userEntity == null) {
				throw exception(
						"User not found",
						"User not found",
						ExceptionType.NOT_FOUND
				);
			}

			return UserMapper.toDomain(userEntity);

		} catch (InfrastructureException | ValueObjectException e) {

			throw e;

		} catch (IllegalArgumentException e) {

			throw exception(
					e.getMessage(),
					"Invalid argument",
					ExceptionType.INVALID_ARGUMENT
			);

		} catch (Exception e) {

			throw exception(
					e.getMessage(),
					"UNKNOWN",
					ExceptionType.INTERNAL_SERVER_ERROR
			);
		}
	}

	private InfrastructureException exception(
			String message,
			String violation,
			int code
	) {
		return new InfrastructureException(
				message,
				UserQueryRepository.class.getSimpleName(),
				violation,
				code
		);
	}


}