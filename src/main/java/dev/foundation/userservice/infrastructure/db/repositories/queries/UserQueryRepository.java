package dev.foundation.userservice.infrastructure.db.repositories.queries;

import dev.foundation.userservice.application.ports.UserQueryRepositoryPort;
import dev.foundation.userservice.application.usecases.user.findByEmail.helpers.UserAndProfilePictureKey;
import dev.foundation.userservice.domain.value_objects.Email;
import dev.foundation.userservice.errors.InfrastructureException;
import dev.foundation.userservice.errors.SystemException;
import dev.foundation.userservice.errors.ValueObjectException;
import dev.foundation.userservice.errors.types.ExceptionType;
import dev.foundation.userservice.infrastructure.db.entities.UserEntity;
import dev.foundation.userservice.infrastructure.db.mappers.UserMapper;
import dev.foundation.userservice.infrastructure.db.projections.UserAndProfilePictureKeyProjection;
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


	public UserAndProfilePictureKey findById(UUID id)
			throws InfrastructureException, ValueObjectException {

		try {
			UserAndProfilePictureKeyProjection entity = entityManager
					.createQuery(
							"""
							SELECT new dev.foundation.userservice.infrastructure.db.projections.UserAndProfilePictureKeyProjection(u, pp.key)
							FROM UserEntity u
							LEFT JOIN ProfilePictureEntity pp
							ON pp.id = u.profilePictureId
							WHERE u.id = :id
							""",
							UserAndProfilePictureKeyProjection.class
					)
					.setParameter("id", id)
					.getSingleResultOrNull();

			if (entity == null) {
				throw exception(
						"User not found",
						"User not found",
						ExceptionType.NOT_FOUND
				);
			}

			return this.atType(entity.userEntity(), entity.key());

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
	public UserAndProfilePictureKey findByEmail(Email email) throws SystemException {
		String parsedEmail = email.value();
		try{
			UserAndProfilePictureKeyProjection entity = this.entityManager.createQuery(
							"""
									SELECT new dev.foundation.userservice.infrastructure.db.projections.UserAndProfilePictureKeyProjection(u, pp.key)
									FROM UserEntity u
									LEFT JOIN ProfilePictureEntity pp
									ON pp.id = u.profilePictureId
									WHERE u.email = :email
									""",UserAndProfilePictureKeyProjection.class)
					.setParameter("email", parsedEmail)
					.getSingleResultOrNull();

			if(entity == null){
				throw exception(
						"User not found",
						"User not found",
						ExceptionType.NOT_FOUND
				);
			}

			return this.atType(entity.userEntity(), entity.key());
		}

		catch (InfrastructureException e){
			throw e;
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
	public List<UserAndProfilePictureKey> findAll(int pageStart, int pageLimit) throws SystemException {
		try{
			List<UserAndProfilePictureKeyProjection> entities = this.entityManager
					.createQuery("""
						SELECT new dev.foundation.userservice.infrastructure.db.projections.UserAndProfilePictureKeyProjection(u, pp.key)
						FROM UserEntity u
						LEFT JOIN ProfilePictureEntity pp
						ON pp.id = u.profilePictureId
						""", UserAndProfilePictureKeyProjection.class)
					.setFirstResult(pageStart)
					.setMaxResults(pageLimit)
					.getResultList();

			List<UserAndProfilePictureKey> list = new ArrayList<>();

			for (UserAndProfilePictureKeyProjection entity : entities) {
				list.add(this.atType(entity.userEntity(),entity.key()));
			}

			return list;
		}
		catch (ValueObjectException e){
			throw e;
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

	private UserAndProfilePictureKey atType(UserEntity userEntity, String key) throws ValueObjectException{
		return new UserAndProfilePictureKey(UserMapper.toDomain(userEntity), key);
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