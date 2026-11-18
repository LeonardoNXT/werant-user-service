package dev.foundation.userservice.application.usecases.user.register;

import dev.foundation.userservice.application.ports.*;
import dev.foundation.userservice.application.services.OutboxCreate.OutboxCreateService;
import dev.foundation.userservice.application.usecases.Usecase;
import dev.foundation.userservice.application.usecases.user.register.helpers.ValidatedInput;
import dev.foundation.userservice.domain.entities.Outbox;
import dev.foundation.userservice.domain.entities.User;
import dev.foundation.userservice.domain.value_objects.*;
import dev.foundation.userservice.errors.ApplicationException;
import dev.foundation.userservice.errors.SystemException;
import dev.foundation.userservice.errors.ValueObjectException;
import dev.foundation.userservice.errors.types.ExceptionType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RegisterUsecase implements Usecase<RegisterInputDto, RegisterOutputDto> {
	private final UserCommandRepositoryPort userCommandRepositoryPort;
	private final IdGeneratorPort idGeneratorPort;
	private final PasswordHasherPort passwordHasherPort;
	private final ClockPort clockPort;

	private RegisterUsecase(UserCommandRepositoryPort userCommandRepositoryPort, PasswordHasherPort passwordHasherPort, IdGeneratorPort idGeneratorPort, ClockPort clockPort) {
		this.userCommandRepositoryPort = userCommandRepositoryPort;
		this.idGeneratorPort = idGeneratorPort;
		this.passwordHasherPort = passwordHasherPort;
		this.clockPort = clockPort;
	}

	public static RegisterUsecase create(UserCommandRepositoryPort userCommandRepositoryPort, PasswordHasherPort passwordHasherPort, IdGeneratorPort idGeneratorPort, ClockPort clockPort) {
		return new RegisterUsecase(userCommandRepositoryPort, passwordHasherPort, idGeneratorPort, clockPort);
	}

	public RegisterOutputDto execute(RegisterInputDto input) throws SystemException {

		ValidatedInput validatedInput = this.validateInput(input.name(), input.username(), input.email(),input.phoneNumber(), input.password());

		User user = this.createUser(validatedInput, input.numberVerified(), input.emailVerified());

		Date outboxDate = this.clockPort.nowIso8601();

		List<Outbox> outboxCreateOutput = OutboxCreateService.execute(user.allDomainEvents(), outboxDate);

		if (outboxCreateOutput.isEmpty()) {
			throw new ApplicationException("No events found. Event generation is required to execute the registration usecase.", RegisterUsecase.class.getSimpleName(), "System Exception", ExceptionType.INTERNAL_SERVER_ERROR);
		}

		userCommandRepositoryPort.persist(user, outboxCreateOutput);

		return new RegisterOutputDto(user.id().toString(), user.email().value(), user.status().value());
	}

	private ValidatedInput validateInput(String inputName, String inputUsername, String inputEmail, Optional<String> inputPhoneNumber, String inputPassword) throws ValueObjectException {
		try{
			Name name = Name.create(inputName);
			Username username = Username.create(inputUsername);
			Email email = Email.create(inputEmail);
			Password password = Password.create(inputPassword);
			PhoneNumber phoneNumber = inputPhoneNumber.isPresent() ? PhoneNumber.create(inputPhoneNumber.get()) : null;
			return new ValidatedInput(name, username, email,phoneNumber, password);
		} catch (ValueObjectException e){
			throw e.at(RegisterUsecase.class.getSimpleName());
		}
	}

	private User createUser(ValidatedInput validInput, boolean numberVerified, boolean emailVerified) throws SystemException {

		UUID userId = this.idGeneratorPort.generate();
		UUID eventId = this.idGeneratorPort.generate();
		Date date = this.clockPort.nowIso8601();

		HashedPassword hashedPassword = this.passwordHasherPort.hash(validInput.password());

		return User.create(userId, validInput.name(), validInput.username(), validInput.email(),validInput.phoneNumber(), hashedPassword, null, emailVerified, numberVerified, date, date, eventId);

	}


}
