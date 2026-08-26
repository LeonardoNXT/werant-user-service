package dev.foundation.userservice.infrastructure.db.repositories.commands;

import dev.foundation.userservice.application.ports.UserCommandRepositoryPort;
import dev.foundation.userservice.domain.entities.Outbox;
import dev.foundation.userservice.domain.entities.User;
import dev.foundation.userservice.domain.value_objects.Date;
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

import java.util.List;
import java.util.UUID;

public class UserCommandRepository extends Repository implements UserCommandRepositoryPort {

	private UserCommandRepository(EntityManager entityManager) {
		super(entityManager);
	}

	public static UserCommandRepository create(EntityManager entityManager) {
		return new UserCommandRepository(entityManager);
	}

	@Override
	public void persist(User user, List<Outbox> outboxes) throws InfrastructureException {
		UserEntity userEntity = UserMapper.toOrm(user);
		List<OutboxEntity> outboxEntities = outboxes.stream().map(OutboxMapper::toOrm).toList();


		try {
			this.transaction((entityManager -> {
				entityManager.persist(userEntity);
				outboxEntities.forEach(entityManager::persist);

				return null;
			}));
		} catch (Exception e) {

			switch (e) {
				case EntityExistsException entityExistsException -> throw new InfrastructureException(
						entityExistsException.getMessage(),
						UserCommandRepository.class.getSimpleName(),
						"User already exists",
						ExceptionType.CONFLICT
				);
				case IllegalArgumentException illegalArgumentException -> throw new InfrastructureException(
						illegalArgumentException.getMessage(),
						UserCommandRepository.class.getSimpleName(),
						"Invalid argument",
						ExceptionType.INVALID_ARGUMENT
				);
				case InfrastructureException infrastructureException -> throw infrastructureException;
				default -> {
					throw new InfrastructureException(
							e.getMessage(),
							UserCommandRepository.class.getSimpleName(),
							"UNKNOWN",
							ExceptionType.INTERNAL_SERVER_ERROR
					);
				}
			}
		}
	}

	@Override
	public User save(User user, List<Outbox> outboxes) throws InfrastructureException,ValueObjectException {
		UserEntity userEntity = UserMapper.toOrm(user);
		List<OutboxEntity> outboxEntities = outboxes.stream().map(OutboxMapper::toOrm).toList();
		try{
			UserEntity entitySaved = this.transaction(entityManager -> {
				UserEntity merge = entityManager.merge(userEntity);
				outboxEntities.forEach(entityManager::persist);

				return merge;
			});

			return UserMapper.toDomain(entitySaved);

		}

		catch (Exception e){

			switch (e) {
				case EntityExistsException entityExistsException -> throw new InfrastructureException(
						entityExistsException.getMessage(),
						UserCommandRepository.class.getSimpleName(),
						"User already exists",
						ExceptionType.CONFLICT
				);
				case IllegalArgumentException illegalArgumentException -> throw new InfrastructureException(
						illegalArgumentException.getMessage(),
						UserCommandRepository.class.getSimpleName(),
						"Invalid argument",
						ExceptionType.INVALID_ARGUMENT
				);
				case ValueObjectException valueObjectException -> throw valueObjectException;
				case InfrastructureException infrastructureException -> throw infrastructureException;
				default -> {
					throw new InfrastructureException(
							e.getMessage(),
							UserCommandRepository.class.getSimpleName(),
							"UNKNOWN",
							ExceptionType.INTERNAL_SERVER_ERROR
					);
				}
			}
		}
	}

	@Override
	public void delete(UUID id, List<Outbox> outboxes) throws InfrastructureException {
		List<OutboxEntity> outboxEntities = outboxes.stream().map(OutboxMapper::toOrm).toList();

		try{
			this.transaction((entityManager) -> {
				UserEntity reference = this.entityManager.getReference(UserEntity.class, id);
				entityManager.remove(reference);
				outboxEntities.forEach(entityManager::persist);

				return null;
			});
		}
		catch (Exception e){

			switch (e) {
				case IllegalArgumentException illegalArgumentException -> throw new InfrastructureException(
						illegalArgumentException.getMessage(),
						UserCommandRepository.class.getSimpleName(),
						"Invalid argument",
						ExceptionType.INVALID_ARGUMENT
				);
				case InfrastructureException infrastructureException -> throw infrastructureException;
				default -> {
					throw new InfrastructureException(
							e.getMessage(),
							UserCommandRepository.class.getSimpleName(),
							"UNKNOWN",
							ExceptionType.INTERNAL_SERVER_ERROR
					);
				}
			}

		}

	}


}
