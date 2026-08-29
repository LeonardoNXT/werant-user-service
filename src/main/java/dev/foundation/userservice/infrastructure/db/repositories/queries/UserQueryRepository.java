package dev.foundation.userservice.infrastructure.db.repositories.queries;

import dev.foundation.userservice.application.ports.UserQueryRepositoryPort;
import dev.foundation.userservice.domain.entities.User;
import dev.foundation.userservice.errors.InfrastructureException;
import dev.foundation.userservice.errors.SystemException;
import dev.foundation.userservice.errors.ValueObjectException;
import dev.foundation.userservice.errors.types.ExceptionType;
import dev.foundation.userservice.infrastructure.db.entities.UserEntity;
import dev.foundation.userservice.infrastructure.db.mappers.UserMapper;
import dev.foundation.userservice.infrastructure.db.repositories.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.QueryTimeoutException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserQueryRepository
		extends Repository implements UserQueryRepositoryPort {

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

	@Override
	public User findByEmail(String email) throws SystemException {
		try{
			UserEntity entity = this.entityManager.createQuery(
							"""
									SELECT u
									FROM UserEntity u
									WHERE u.email = :email
									""",UserEntity.class)
					.setParameter("email", email)
					.getSingleResultOrNull();

			return UserMapper.toDomain(entity);
		}
		catch (IllegalArgumentException e){
			throw exception(
					"This is not a valid query.",
					"INVALID ARGUMENT",
					ExceptionType.INVALID_ARGUMENT
			);
		}
		catch (QueryTimeoutException e){
			throw exception(
					"Query timeout",
					"UNKNOWN",
					ExceptionType.TIME_OUT_ERROR
			);
		}
		catch (PersistenceException e){
			throw exception(
					"There was an error as the query running.",
					"UNKNOWN",
					ExceptionType.INTERNAL_SERVER_ERROR
			);
		}
		catch (Exception e) {

			throw exception(
					e.getMessage(),
					"UNKNOWN",
					ExceptionType.INTERNAL_SERVER_ERROR
			);
		}
	}

	@Override
	public List<User> findAll(int pageStart, int pageLimit) throws SystemException {
		try{
			List<UserEntity> entities = this.entityManager
					.createQuery("""
						SELECT u
						FROM UserEntity u
						ORDER BY u.created_at DESC
						""", UserEntity.class)
					.setFirstResult(pageStart)
					.setMaxResults(pageLimit)
					.getResultList();

			List<User> list = new ArrayList<>();

			for (UserEntity entity : entities) {
				User domain = UserMapper.toDomain(entity);
				list.add(domain);
			}

			return list;
		}
		catch (IllegalArgumentException e){
			throw exception(
					"This is not a valid query.",
					"INVALID ARGUMENT",
					ExceptionType.INVALID_ARGUMENT
			);
		}
		catch (QueryTimeoutException e){
			throw exception(
					"Query timeout",
					"UNKNOWN",
					ExceptionType.TIME_OUT_ERROR
			);
		}
		catch (PersistenceException e){
			throw exception(
					"There was an error as the query running.",
					"UNKNOWN",
					ExceptionType.INTERNAL_SERVER_ERROR
			);
		}
		catch (Exception e) {
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