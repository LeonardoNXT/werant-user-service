package dev.foundation.userservice.application.usecase;

import dev.foundation.userservice.application.ports.ClockPort;
import dev.foundation.userservice.application.ports.IdGeneratorPort;
import dev.foundation.userservice.application.ports.PasswordHasherPort;
import dev.foundation.userservice.application.ports.UserCommandRepositoryPort;
import dev.foundation.userservice.application.usecases.register.RegisterInputDto;
import dev.foundation.userservice.application.usecases.register.RegisterOutputDto;
import dev.foundation.userservice.application.usecases.register.RegisterUsecase;
import dev.foundation.userservice.domain.entities.User;
import dev.foundation.userservice.domain.value_objects.Date;
import dev.foundation.userservice.domain.value_objects.HashedPassword;
import dev.foundation.userservice.domain.value_objects.Password;
import dev.foundation.userservice.errors.InfrastructureException;
import dev.foundation.userservice.errors.SystemException;
import dev.foundation.userservice.errors.ValueObjectException;
import dev.foundation.userservice.errors.types.ExceptionType;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RegisterUsecaseTest {

	private static final UUID USER_ID = UUID.randomUUID();
	private static final UUID EVENT_ID = UUID.randomUUID();

	private static final String DATE =
			"2026-12-01T00:00:00Z";

	private static final String NAME =
			"Leonardo Silva";

	private static final String USERNAME =
			"leonardox";

	private static final String EMAIL =
			"leo@email.com";

	private static final String PHONE =
			"(21) 97981-8980";

	private static final String PASSWORD =
			"Password123!";

	private static final String HASHED_PASSWORD =
			"$2a$10$hashedpassword";

	private final UserCommandRepositoryPort userCommandRepositoryPort =
			mock(UserCommandRepositoryPort.class);

	private final PasswordHasherPort passwordHasherPort =
			mock(PasswordHasherPort.class);

	private final IdGeneratorPort idGeneratorPort =
			mock(IdGeneratorPort.class);

	private final ClockPort clockPort =
			mock(ClockPort.class);


	/*
	 * -------------------------------------------------------------------------
	 * Fixtures
	 * -------------------------------------------------------------------------
	 */

	private RegisterUsecase createUsecase() {
		return RegisterUsecase.create(
				userCommandRepositoryPort,
				passwordHasherPort,
				idGeneratorPort,
				clockPort
		);
	}

	private RegisterInputDto createInput() {
		return new RegisterInputDto(
				NAME,
				USERNAME,
				EMAIL,
				Optional.of(PHONE),
				PASSWORD,
				false,
				false
		);
	}

	private void mockDefaultDependencies()
			throws SystemException {

		Date date = Date.create(DATE);

		when(idGeneratorPort.generate())
				.thenReturn(USER_ID, EVENT_ID);

		when(clockPort.nowIso8601())
				.thenReturn(date, date);

		when(passwordHasherPort.hash(any(Password.class)))
				.thenReturn(
						HashedPassword.create(HASHED_PASSWORD)
				);
	}


	/*
	 * -------------------------------------------------------------------------
	 * Creation
	 * -------------------------------------------------------------------------
	 */

	@Test
	void shouldCreateRegisterUsecase() {

		RegisterUsecase usecase = createUsecase();

		assertNotNull(usecase);
	}


	/*
	 * -------------------------------------------------------------------------
	 * Registration
	 * -------------------------------------------------------------------------
	 */

	@Test
	void shouldRegisterUser() throws Exception {

		mockDefaultDependencies();

		RegisterUsecase usecase = createUsecase();

		RegisterOutputDto output =
				usecase.execute(createInput());

		assertNotNull(output);

		assertEquals(
				USER_ID.toString(),
				output.id()
		);

		assertEquals(
				EMAIL,
				output.email()
		);

		assertEquals(
				"active",
				output.status()
		);
	}


	@Test
	void shouldGenerateUserIdAndEventId() throws Exception {

		mockDefaultDependencies();

		RegisterUsecase usecase = createUsecase();

		usecase.execute(createInput());

		verify(
				idGeneratorPort,
				times(2)
		).generate();
	}


	@Test
	void shouldHashPassword() throws Exception {

		mockDefaultDependencies();

		RegisterUsecase usecase = createUsecase();

		usecase.execute(createInput());

		verify(passwordHasherPort)
				.hash(any(Password.class));
	}


	@Test
	void shouldUseCurrentDate() throws Exception {

		mockDefaultDependencies();

		RegisterUsecase usecase = createUsecase();

		usecase.execute(createInput());

		verify(
				clockPort,
				times(2)
		).nowIso8601();
	}


	/*
	 * -------------------------------------------------------------------------
	 * Persistence
	 * -------------------------------------------------------------------------
	 */

	@Test
	void shouldPersistUserAndOutbox() throws Exception {

		mockDefaultDependencies();

		RegisterUsecase usecase = createUsecase();

		usecase.execute(createInput());

		verify(userCommandRepositoryPort)
				.persist(
						any(User.class),
						anyList()
				);
	}


	@Test
	void shouldPersistExactlyOneOutbox() throws Exception {

		mockDefaultDependencies();

		RegisterUsecase usecase = createUsecase();

		usecase.execute(createInput());

		verify(userCommandRepositoryPort)
				.persist(
						any(User.class),
						argThat(outboxes ->
								outboxes.size() == 1
						)
				);
	}


	@Test
	void shouldPersistUserWithGeneratedId() throws Exception {

		mockDefaultDependencies();

		RegisterUsecase usecase = createUsecase();

		usecase.execute(createInput());

		verify(userCommandRepositoryPort)
				.persist(
						argThat(user ->
								user.id().equals(USER_ID)
						),
						anyList()
				);
	}


	/*
	 * -------------------------------------------------------------------------
	 * User verification flags
	 * -------------------------------------------------------------------------
	 *
	 * The RegisterInputDto currently exposes two boolean values.
	 * The tests below assume:
	 *
	 *   boolean #1 -> emailVerified
	 *   boolean #2 -> numberVerified
	 *
	 * If your DTO uses another meaning, adjust the fixtures accordingly.
	 */

	@Test
	void shouldPreserveEmailVerifiedValue() throws Exception {

		mockDefaultDependencies();

		RegisterInputDto input =
				new RegisterInputDto(
						NAME,
						USERNAME,
						EMAIL,
						Optional.of(PHONE),
						PASSWORD,
						true,
						false
				);

		RegisterUsecase usecase = createUsecase();

		usecase.execute(input);

		verify(userCommandRepositoryPort)
				.persist(
						argThat(User::emailVerified),
						anyList()
				);
	}


	@Test
	void shouldPreserveNumberVerifiedValue() throws Exception {

		mockDefaultDependencies();

		RegisterInputDto input =
				new RegisterInputDto(
						NAME,
						USERNAME,
						EMAIL,
						Optional.of(PHONE),
						PASSWORD,
						false,
						true
				);

		RegisterUsecase usecase = createUsecase();

		usecase.execute(input);

		verify(userCommandRepositoryPort)
				.persist(
						argThat(User::numberVerified),
						anyList()
				);
	}


	/*
	 * -------------------------------------------------------------------------
	 * Outbox
	 * -------------------------------------------------------------------------
	 */

	@Test
	void shouldCreateOutboxWithPendingStatus() throws Exception {

		mockDefaultDependencies();

		RegisterUsecase usecase = createUsecase();

		usecase.execute(createInput());

		verify(userCommandRepositoryPort)
				.persist(
						any(User.class),
						argThat(outboxes ->
								outboxes.size() == 1 &&
										outboxes
												.getFirst()
												.status()
												.equals("pending")
						)
				);
	}


	@Test
	void shouldCreateOutboxWithUserAggregateId() throws Exception {

		mockDefaultDependencies();

		RegisterUsecase usecase = createUsecase();

		usecase.execute(createInput());

		verify(userCommandRepositoryPort)
				.persist(
						any(User.class),
						argThat(outboxes ->
								outboxes.size() == 1 &&
										outboxes
												.getFirst()
												.aggregateId()
												.equals(USER_ID)
						)
				);
	}


	/*
	 * -------------------------------------------------------------------------
	 * Invalid input
	 * -------------------------------------------------------------------------
	 */

	@Test
	void shouldRejectInvalidName() throws Exception {

		mockDefaultDependencies();

		RegisterInputDto input =
				new RegisterInputDto(
						"",
						USERNAME,
						EMAIL,
						Optional.of(PHONE),
						PASSWORD,
						false,
						false
				);

		RegisterUsecase usecase = createUsecase();

		assertThrows(
				ValueObjectException.class,
				() -> usecase.execute(input)
		);

		verifyNoInteractions(passwordHasherPort);
		verifyNoInteractions(userCommandRepositoryPort);
	}


	@Test
	void shouldRejectInvalidUsername() throws Exception {

		mockDefaultDependencies();

		RegisterInputDto input =
				new RegisterInputDto(
						NAME,
						"",
						EMAIL,
						Optional.of(PHONE),
						PASSWORD,
						false,
						false
				);

		RegisterUsecase usecase = createUsecase();

		assertThrows(
				ValueObjectException.class,
				() -> usecase.execute(input)
		);

		verifyNoInteractions(passwordHasherPort);
		verifyNoInteractions(userCommandRepositoryPort);
	}


	@Test
	void shouldRejectInvalidEmail() throws Exception {

		mockDefaultDependencies();

		RegisterInputDto input =
				new RegisterInputDto(
						NAME,
						USERNAME,
						"invalid-email",
						Optional.of(PHONE),
						PASSWORD,
						false,
						false
				);

		RegisterUsecase usecase = createUsecase();

		assertThrows(
				ValueObjectException.class,
				() -> usecase.execute(input)
		);

		verifyNoInteractions(passwordHasherPort);
		verifyNoInteractions(userCommandRepositoryPort);
	}


	@Test
	void shouldRejectInvalidPassword() throws Exception {

		mockDefaultDependencies();

		RegisterInputDto input =
				new RegisterInputDto(
						NAME,
						USERNAME,
						EMAIL,
						Optional.of(PHONE),
						"",
						false,
						false
				);

		RegisterUsecase usecase = createUsecase();

		assertThrows(
				ValueObjectException.class,
				() -> usecase.execute(input)
		);

		verifyNoInteractions(passwordHasherPort);
		verifyNoInteractions(userCommandRepositoryPort);
	}


	@Test
	void shouldNotPersistWhenInputIsInvalid() throws Exception {

		mockDefaultDependencies();

		RegisterInputDto input =
				new RegisterInputDto(
						"",
						USERNAME,
						EMAIL,
						Optional.of(PHONE),
						PASSWORD,
						false,
						false
				);

		RegisterUsecase usecase = createUsecase();

		assertThrows(
				ValueObjectException.class,
				() -> usecase.execute(input)
		);

		verify(
				userCommandRepositoryPort,
				never()
		).persist(
				any(User.class),
				anyList()
		);
	}


	/*
	 * -------------------------------------------------------------------------
	 * Infrastructure errors
	 * -------------------------------------------------------------------------
	 */

	@Test
	void shouldPropagateInfrastructureException()
			throws Exception {

		mockDefaultDependencies();

		doThrow(
				new InfrastructureException(
						"Persistence failed",
						RegisterUsecase.class.getSimpleName(),
						"Infrastructure Exception",
						ExceptionType.INTERNAL_SERVER_ERROR
				)
		).when(userCommandRepositoryPort)
				.persist(
						any(User.class),
						anyList()
				);

		RegisterUsecase usecase = createUsecase();

		assertThrows(
				InfrastructureException.class,
				() -> usecase.execute(createInput())
		);
	}
}