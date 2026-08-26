
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
import dev.foundation.userservice.errors.ValueObjectException;
import dev.foundation.userservice.errors.types.ExceptionType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RegisterUsecaseTest {

	private static final UUID USER_ID = UUID.randomUUID();
	private static final UUID EVENT_ID = UUID.randomUUID();
	private static final String DATE = "2026-12-01T00:00:00Z";

	private final UserCommandRepositoryPort userCommandRepositoryPort =
			mock(UserCommandRepositoryPort.class);

	private final PasswordHasherPort passwordHasherPort =
			mock(PasswordHasherPort.class);

	private final IdGeneratorPort idGeneratorPort =
			mock(IdGeneratorPort.class);

	private final ClockPort clockPort =
			mock(ClockPort.class);

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
				"Leonardo Silva",
				"leonardox",
				"leo@email.com",
				"Password123!",
				"user",
				null,
				false,
				false
		);
	}

	private void mockDefaultDependencies() throws ValueObjectException {
		Date date = Date.create(DATE);

		when(idGeneratorPort.generate())
				.thenReturn(USER_ID, EVENT_ID);

		when(clockPort.nowIso8601())
				.thenReturn(date, date);

		when(passwordHasherPort.hash(any(Password.class)))
				.thenReturn(HashedPassword.create("$2a$10$hashedpassword"));
	}

	@Test
	void shouldCreateRegisterUsecase() {
		RegisterUsecase usecase = createUsecase();

		assertNotNull(usecase);
	}

	@Test
	void shouldRegisterUser() throws Exception {
		mockDefaultDependencies();

		RegisterUsecase usecase = createUsecase();

		RegisterOutputDto output = usecase.execute(createInput());

		assertNotNull(output);
		assertEquals(USER_ID.toString(), output.id());
		assertEquals("leo@email.com", output.email());
		assertEquals("user", output.role());
		assertEquals("active", output.status());
	}

	@Test
	void shouldGenerateUserIdAndEventId() throws Exception {
		mockDefaultDependencies();

		RegisterUsecase usecase = createUsecase();

		usecase.execute(createInput());

		verify(idGeneratorPort, times(2)).generate();
	}

	@Test
	void shouldHashPassword() throws Exception {
		mockDefaultDependencies();

		RegisterUsecase usecase = createUsecase();

		usecase.execute(createInput());

		verify(passwordHasherPort).hash(any(Password.class));
	}

	@Test
	void shouldUseCurrentDate() throws Exception {
		mockDefaultDependencies();

		RegisterUsecase usecase = createUsecase();

		usecase.execute(createInput());

		verify(clockPort, times(2)).nowIso8601();
	}

	@Test
	void shouldPersistUserAndOutbox() throws Exception {
		mockDefaultDependencies();

		RegisterUsecase usecase = createUsecase();

		usecase.execute(createInput());

		verify(userCommandRepositoryPort).persist(
				any(User.class),
				anyList()
		);
	}

	@Test
	void shouldPersistExactlyOneOutbox() throws Exception {
		mockDefaultDependencies();

		RegisterUsecase usecase = createUsecase();

		usecase.execute(createInput());

		verify(userCommandRepositoryPort).persist(
				any(User.class),
				argThat(outboxes -> outboxes.size() == 1)
		);
	}

	@Test
	void shouldPersistUserWithGeneratedId() throws Exception {
		mockDefaultDependencies();

		RegisterUsecase usecase = createUsecase();

		usecase.execute(createInput());

		verify(userCommandRepositoryPort).persist(
				argThat(user -> user.id().equals(USER_ID)),
				anyList()
		);
	}

	@Test
	void shouldUseUserRoleWhenRoleIsNotProvided() throws Exception {
		mockDefaultDependencies();

		RegisterInputDto input = new RegisterInputDto(
				"Leonardo Silva",
				"leonardox",
				"leo@email.com",
				"Password123!",
				null,
				null,
				false,
				false
		);

		RegisterUsecase usecase = createUsecase();

		RegisterOutputDto output = usecase.execute(input);

		assertEquals("user", output.role());
	}

	@Test
	void shouldUseUserRoleWhenRoleIsEmpty() throws Exception {
		mockDefaultDependencies();

		RegisterInputDto input = new RegisterInputDto(
				"Leonardo Silva",
				"leonardox",
				"leo@email.com",
				"Password123!",
				"",
				null,
				false,
				false
		);

		RegisterUsecase usecase = createUsecase();

		RegisterOutputDto output = usecase.execute(input);

		assertEquals("user", output.role());
	}

	@Test
	void shouldPreserveEmailVerifiedValue() throws Exception {
		mockDefaultDependencies();

		RegisterInputDto input = new RegisterInputDto(
				"Leonardo Silva",
				"leonardox",
				"leo@email.com",
				"Password123!",
				"user",
				null,
				true,
				false
		);

		RegisterUsecase usecase = createUsecase();

		usecase.execute(input);

		verify(userCommandRepositoryPort).persist(
				argThat(User::emailVerified),
				anyList()
		);
	}

	@Test
	void shouldPreserveNumberVerifiedValue() throws Exception {
		mockDefaultDependencies();

		RegisterInputDto input = new RegisterInputDto(
				"Leonardo Silva",
				"leonardox",
				"leo@email.com",
				"Password123!",
				"user",
				null,
				false,
				true
		);

		RegisterUsecase usecase = createUsecase();

		usecase.execute(input);

		verify(userCommandRepositoryPort).persist(
				argThat(User::numberVerified),
				anyList()
		);
	}

	@Test
	void shouldCreateOutboxWithPendingStatus() throws Exception {
		mockDefaultDependencies();

		RegisterUsecase usecase = createUsecase();

		usecase.execute(createInput());

		verify(userCommandRepositoryPort).persist(
				any(User.class),
				argThat(outboxes ->
						outboxes.size() == 1 &&
								outboxes.getFirst().status().equals("pending")
				)
		);
	}

	@Test
	void shouldCreateOutboxWithUserAggregateId() throws Exception {
		mockDefaultDependencies();

		RegisterUsecase usecase = createUsecase();

		usecase.execute(createInput());

		verify(userCommandRepositoryPort).persist(
				any(User.class),
				argThat(outboxes ->
						outboxes.size() == 1 &&
								outboxes.getFirst().aggregateId().equals(USER_ID)
				)
		);
	}

	@Test
	void shouldRejectInvalidName() throws Exception {
		mockDefaultDependencies();

		RegisterInputDto input = new RegisterInputDto(
				"",
				"leonardox",
				"leo@email.com",
				"Password123!",
				"user",
				null,
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

		RegisterInputDto input = new RegisterInputDto(
				"Leonardo Silva",
				"abc",
				"leo@email.com",
				"Password123!",
				"user",
				null,
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

		RegisterInputDto input = new RegisterInputDto(
				"Leonardo Silva",
				"leonardox",
				"invalid-email",
				"Password123!",
				"user",
				null,
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

		RegisterInputDto input = new RegisterInputDto(
				"Leonardo Silva",
				"leonardox",
				"leo@email.com",
				"invalid",
				"user",
				null,
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
	void shouldRejectInvalidRole() throws Exception {
		mockDefaultDependencies();

		RegisterInputDto input = new RegisterInputDto(
				"Leonardo Silva",
				"leonardox",
				"leo@email.com",
				"Password123!",
				"invalid-role",
				null,
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

		RegisterInputDto input = new RegisterInputDto(
				"invalid",
				"leonardox",
				"leo@email.com",
				"Password123!",
				"user",
				null,
				false,
				false
		);

		RegisterUsecase usecase = createUsecase();

		assertThrows(
				ValueObjectException.class,
				() -> usecase.execute(input)
		);

		verify(userCommandRepositoryPort, never())
				.persist(any(User.class), anyList());
	}

	@Test
	void shouldPropagateInfrastructureException() throws Exception {
		mockDefaultDependencies();

		doThrow(new InfrastructureException(
				"Persistence failed",
				RegisterUsecase.class.getSimpleName(),
				"Infrastructure Exception",
				ExceptionType.INTERNAL_SERVER_ERROR
		)).when(userCommandRepositoryPort)
				.persist(any(User.class), anyList());

		RegisterUsecase usecase = createUsecase();

		assertThrows(
				InfrastructureException.class,
				() -> usecase.execute(createInput())
		);
	}
}