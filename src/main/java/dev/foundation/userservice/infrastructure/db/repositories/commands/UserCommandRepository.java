package dev.foundation.userservice.infrastructure.db.repositories.commands;

import dev.foundation.userservice.application.ports.UserCommandRepositoryPort;
import dev.foundation.userservice.domain.entities.Outbox;
import dev.foundation.userservice.domain.entities.User;
import dev.foundation.userservice.errors.InfrastructureException;
import dev.foundation.userservice.errors.ValueObjectException;
import dev.foundation.userservice.errors.types.ExceptionType;
import dev.foundation.userservice.infrastructure.db.entities.OutboxEntity;
import dev.foundation.userservice.infrastructure.db.entities.UserEntity;
import dev.foundation.userservice.infrastructure.db.mappers.OutboxMapper;
import dev.foundation.userservice.infrastructure.db.mappers.UserMapper;
import dev.foundation.userservice.infrastructure.db.repositories.Repository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public class UserCommandRepository
		extends Repository
		implements UserCommandRepositoryPort {

	protected UserCommandRepository(EntityManager entityManager) {
		super(entityManager);
	}

	public static UserCommandRepository create(EntityManager entityManager) {
		return new UserCommandRepository(entityManager);
	}

	@Override
	@Transactional(rollbackFor = InfrastructureException.class)
	public void persist(
			User user,
			List<Outbox> outboxes
	) throws InfrastructureException {

		UserEntity userEntity = UserMapper.toOrm(user);

		List<OutboxEntity> outboxEntities = outboxes.stream()
				.map(OutboxMapper::toOrm)
				.toList();

		try {
			entityManager.persist(userEntity);

			outboxEntities.forEach(entityManager::persist);

			entityManager.flush();

		}
		catch (ConstraintViolationException e){

			throw new InfrastructureException(
					"The email is already in use",
					e.getClass().getSimpleName(),
					"CONFLICT",
					ExceptionType.CONFLICT
			);

		}
		catch (EntityExistsException e) {

			throw new InfrastructureException(
					e.getMessage(),
					UserCommandRepository.class.getSimpleName(),
					"User already exists",
					ExceptionType.CONFLICT
			);
		}
		catch (IllegalArgumentException e) {

			throw new InfrastructureException(
					e.getMessage(),
					UserCommandRepository.class.getSimpleName(),
					"INVALID ARGUMENT",
					ExceptionType.INVALID_ARGUMENT
			);

		}
		catch (Exception e) {

			throw new InfrastructureException(
					e.getMessage(),
					e.getClass().getSimpleName(),
					"dsadsa",
					ExceptionType.INTERNAL_SERVER_ERROR
			);
		}
	}

	@Override
	@Transactional
	public User save(
			User user,
			List<Outbox> outboxes
	) throws InfrastructureException, ValueObjectException {

		UserEntity userEntity = UserMapper.toOrm(user);

		List<OutboxEntity> outboxEntities = outboxes.stream()
				.map(OutboxMapper::toOrm)
				.toList();

		try {
			UserEntity entitySaved = entityManager.merge(userEntity);

			outboxEntities.forEach(entityManager::persist);

			return UserMapper.toDomain(entitySaved);

		} catch (EntityExistsException e) {

			throw new InfrastructureException(
					e.getMessage(),
					UserCommandRepository.class.getSimpleName(),
					"User already exists",
					ExceptionType.CONFLICT
			);

		} catch (IllegalArgumentException e) {

			throw new InfrastructureException(
					e.getMessage(),
					UserCommandRepository.class.getSimpleName(),
					"Invalid argument",
					ExceptionType.INVALID_ARGUMENT
			);

		} catch (ValueObjectException e) {
			throw e;

		} catch (Exception e) {

			throw new InfrastructureException(
					e.getMessage(),
					UserCommandRepository.class.getSimpleName(),
					"UNKNOWN",
					ExceptionType.INTERNAL_SERVER_ERROR
			);
		}
	}

	@Override
	@Transactional
	public void delete(
			UUID id,
			List<Outbox> outboxes
	) throws InfrastructureException {

		List<OutboxEntity> outboxEntities = outboxes.stream()
				.map(OutboxMapper::toOrm)
				.toList();

		try {
			UserEntity reference =
					entityManager.getReference(UserEntity.class, id);

			entityManager.remove(reference);

			outboxEntities.forEach(entityManager::persist);

		} catch (IllegalArgumentException e) {

			throw new InfrastructureException(
					e.getMessage(),
					UserCommandRepository.class.getSimpleName(),
					"Invalid argument",
					ExceptionType.INVALID_ARGUMENT
			);

		} catch (Exception e) {

			throw new InfrastructureException(
					e.getMessage(),
					UserCommandRepository.class.getSimpleName(),
					"UNKNOWN",
					ExceptionType.INTERNAL_SERVER_ERROR
			);
		}
	}
}