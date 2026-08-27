package dev.foundation.userservice.domain.usecases;

import dev.foundation.userservice.domain.entities.User;
import dev.foundation.userservice.domain.events.*;
import dev.foundation.userservice.domain.shared.DomainEvent;
import dev.foundation.userservice.domain.value_objects.*;
import dev.foundation.userservice.errors.ValueObjectException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

	private static final String CREATED_AT = "2026-12-01T00:00:00Z";
	private static final String UPDATED_AT = "2026-12-01T00:00:00Z";
	private static final String NEXT_UPDATE = "2026-12-02T00:00:00Z";

	private User createUser() throws ValueObjectException {
		return User.create(
				UUID.randomUUID(),
				Name.create("Leonardo Silva"),
				Username.create("leonardo"),
				Email.create("leo@email.com"),
				PhoneNumber.create("21999999999"),
				HashedPassword.create("$2a$10$hashedpassword"),
				UUID.randomUUID(),
				false,
				false,
				Date.create(CREATED_AT),
				Date.create(UPDATED_AT),
				UUID.randomUUID()
		);
	}

	private User restoreUser(UUID id) throws ValueObjectException {
		return User.restore(
				id,
				Name.create("Leonardo Silva"),
				Username.create("leonardox"),
				UserStatus.create("active"),
				Email.create("leo@email.com"),
				PhoneNumber.create("21999999999"),
				HashedPassword.create("$2a$10$hashedpassword"),
				UUID.randomUUID(),
				false,
				false,
				Date.create(CREATED_AT),
				Date.create(UPDATED_AT)
		);
	}

	@Test
	void shouldCreateUser() throws ValueObjectException {
		User user = createUser();

		assertNotNull(user);
	}

	@Test
	void shouldCreateUserWithActiveStatus() throws ValueObjectException {
		User user = createUser();

		assertEquals("active", user.status().value());
	}

	@Test
	void shouldPreserveProvidedValuesWhenCreatingUser() throws ValueObjectException {
		UUID id = UUID.randomUUID();
		UUID profilePictureId = UUID.randomUUID();

		Name name = Name.create("Leonardo Silva");
		Username username = Username.create("leonardox");
		Email email = Email.create("leo@email.com");
		PhoneNumber phoneNumber = PhoneNumber.create("21999999999");
		HashedPassword password = HashedPassword.create("$2a$10$hashedpassword");
		Date createdAt = Date.create(CREATED_AT);
		Date updatedAt = Date.create(UPDATED_AT);

		User user = User.create(
				id,
				name,
				username,
				email,
				phoneNumber,
				password,
				profilePictureId,
				true,
				false,
				createdAt,
				updatedAt,
				UUID.randomUUID()
		);

		assertEquals(id, user.id());
		assertEquals(name, user.name());
		assertEquals(username, user.username());
		assertEquals(email, user.email());
		assertEquals(phoneNumber, user.phoneNumber());
		assertEquals(password, user.password());
		assertEquals(profilePictureId, user.profilePictureId());
		assertTrue(user.emailVerified());
		assertFalse(user.numberVerified());
		assertEquals(createdAt, user.createdAt());
		assertEquals(updatedAt, user.updatedAt());
	}

	@Test
	void shouldCreateCreateUserDomainEvent() throws ValueObjectException {
		UUID eventId = UUID.randomUUID();
		UUID userId = UUID.randomUUID();
		Date createdAt = Date.create(CREATED_AT);

		User user = User.create(
				userId,
				Name.create("Leonardo Silva"),
				Username.create("leonardox"),
				Email.create("leo@email.com"),
				PhoneNumber.create("21999999999"),
				HashedPassword.create("$2a$10$hashedpassword"),
				UUID.randomUUID(),
				false,
				false,
				createdAt,
				Date.create(UPDATED_AT),
				eventId
		);

		assertEquals(1, user.allDomainEvents().size());

		DomainEvent event = user.allDomainEvents().getFirst();

		assertInstanceOf(CreateUserEvent.class, event);
		assertEquals(eventId, event.eventId());
		assertEquals(userId, event.aggregateId());
		assertEquals(createdAt, event.occurredAt());
	}

	@Test
	void shouldRestoreUser() throws ValueObjectException {
		UUID id = UUID.randomUUID();

		User user = restoreUser(id);

		assertNotNull(user);
		assertEquals(id, user.id());
	}

	@Test
	void shouldRestoreUserWithProvidedStatus() throws ValueObjectException {
		User user = User.restore(
				UUID.randomUUID(),
				Name.create("Leonardo Silva"),
				Username.create("leonardox"),
				UserStatus.create("banned"),
				Email.create("leo@email.com"),
				PhoneNumber.create("21999999999"),
				HashedPassword.create("$2a$10$hashedpassword"),
				UUID.randomUUID(),
				false,
				false,
				Date.create(CREATED_AT),
				Date.create(UPDATED_AT)
		);

		assertEquals("banned", user.status().value());
	}

	@Test
	void shouldRestoreUserWithoutDomainEvents() throws ValueObjectException {
		User user = restoreUser(UUID.randomUUID());

		assertTrue(user.allDomainEvents().isEmpty());
	}

	@Test
	void shouldChangeEmail() throws ValueObjectException {
		User original = createUser();

		Email newEmail = Email.create("new@email.com");
		Date date = Date.create(NEXT_UPDATE);

		User updated = original.changeEmail(
				newEmail,
				UUID.randomUUID(),
				date
		);

		assertEquals(newEmail, updated.email());
		assertEquals(date, updated.updatedAt());
	}

	@Test
	void shouldNotMutateOriginalUserWhenChangingEmail() throws ValueObjectException {
		User original = createUser();

		Email originalEmail = original.email();

		User updated = original.changeEmail(
				Email.create("new@email.com"),
				UUID.randomUUID(),
				Date.create(NEXT_UPDATE)
		);

		assertEquals(originalEmail, original.email());
		assertNotEquals(original.email(), updated.email());
	}

	@Test
	void shouldPreserveOtherValuesWhenChangingEmail() throws ValueObjectException {
		User original = createUser();

		User updated = original.changeEmail(
				Email.create("new@email.com"),
				UUID.randomUUID(),
				Date.create(NEXT_UPDATE)
		);

		assertEquals(original.id(), updated.id());
		assertEquals(original.name(), updated.name());
		assertEquals(original.username(), updated.username());
		assertEquals(original.status(), updated.status());
		assertEquals(original.phoneNumber(), updated.phoneNumber());
		assertEquals(original.password(), updated.password());
		assertEquals(original.profilePictureId(), updated.profilePictureId());
		assertEquals(original.emailVerified(), updated.emailVerified());
		assertEquals(original.numberVerified(), updated.numberVerified());
		assertEquals(original.createdAt(), updated.createdAt());
	}

	@Test
	void shouldCreateChangeEmailDomainEvent() throws ValueObjectException {
		UUID eventId = UUID.randomUUID();
		Date date = Date.create(NEXT_UPDATE);

		User updated = createUser().changeEmail(
				Email.create("new@email.com"),
				eventId,
				date
		);

		assertEquals(1, updated.allDomainEvents().size());

		DomainEvent event = updated.allDomainEvents().getFirst();

		assertInstanceOf(ChangeEmailEvent.class, event);
		assertEquals(eventId, event.eventId());
		assertEquals(updated.id(), event.aggregateId());
		assertEquals(date, event.occurredAt());
	}

	// =========================================================
	// Change Phone Number
	// =========================================================

	@Test
	void shouldChangePhoneNumber() throws ValueObjectException {
		User original = createUser();

		PhoneNumber newPhoneNumber = PhoneNumber.create("21979818980");
		Date date = Date.create(NEXT_UPDATE);

		User updated = original.changePhoneNumber(
				newPhoneNumber,
				UUID.randomUUID(),
				date
		);

		assertEquals(newPhoneNumber, updated.phoneNumber());
		assertEquals(date, updated.updatedAt());
	}

	@Test
	void shouldNotMutateOriginalUserWhenChangingPhoneNumber() throws ValueObjectException {
		User original = createUser();

		PhoneNumber originalPhoneNumber = original.phoneNumber();

		User updated = original.changePhoneNumber(
				PhoneNumber.create("21979818980"),
				UUID.randomUUID(),
				Date.create(NEXT_UPDATE)
		);

		assertEquals(originalPhoneNumber, original.phoneNumber());
		assertNotEquals(original.phoneNumber(), updated.phoneNumber());
	}

	@Test
	void shouldPreserveOtherValuesWhenChangingPhoneNumber() throws ValueObjectException {
		User original = createUser();

		User updated = original.changePhoneNumber(
				PhoneNumber.create("21979818980"),
				UUID.randomUUID(),
				Date.create(NEXT_UPDATE)
		);

		assertEquals(original.id(), updated.id());
		assertEquals(original.name(), updated.name());
		assertEquals(original.username(), updated.username());
		assertEquals(original.status(), updated.status());
		assertEquals(original.email(), updated.email());
		assertEquals(original.password(), updated.password());
		assertEquals(original.profilePictureId(), updated.profilePictureId());
		assertEquals(original.emailVerified(), updated.emailVerified());
		assertEquals(original.numberVerified(), updated.numberVerified());
		assertEquals(original.createdAt(), updated.createdAt());
	}

	@Test
	void shouldCreateChangePhoneNumberDomainEvent() throws ValueObjectException {
		UUID eventId = UUID.randomUUID();
		Date date = Date.create(NEXT_UPDATE);

		User updated = createUser().changePhoneNumber(
				PhoneNumber.create("21979818980"),
				eventId,
				date
		);

		assertEquals(1, updated.allDomainEvents().size());

		DomainEvent event = updated.allDomainEvents().getFirst();

		assertInstanceOf(ChangePhoneNumberEvent.class, event);
		assertEquals(eventId, event.eventId());
		assertEquals(updated.id(), event.aggregateId());
		assertEquals(date, event.occurredAt());
	}

	@Test
	void shouldChangePassword() throws ValueObjectException {
		User original = createUser();

		HashedPassword newPassword = HashedPassword.create("$2a$10$newhashedpassword");
		Date date = Date.create(NEXT_UPDATE);

		User updated = original.changePassword(
				newPassword,
				UUID.randomUUID(),
				date
		);

		assertEquals(newPassword, updated.password());
		assertEquals(date, updated.updatedAt());
	}

	@Test
	void shouldNotMutateOriginalUserWhenChangingPassword() throws ValueObjectException {
		User original = createUser();

		HashedPassword originalPassword = original.password();

		User updated = original.changePassword(
				HashedPassword.create("$2a$10$newhashedpassword"),
				UUID.randomUUID(),
				Date.create(NEXT_UPDATE)
		);

		assertEquals(originalPassword, original.password());
		assertNotEquals(original.password(), updated.password());
	}

	@Test
	void shouldCreateChangePasswordDomainEvent() throws ValueObjectException {
		UUID eventId = UUID.randomUUID();
		Date date = Date.create(NEXT_UPDATE);

		User updated = createUser().changePassword(
				HashedPassword.create("$2a$10$newhashedpassword"),
				eventId,
				date
		);

		assertEquals(1, updated.allDomainEvents().size());

		DomainEvent event = updated.allDomainEvents().getFirst();

		assertInstanceOf(ChangePasswordEvent.class, event);
		assertEquals(eventId, event.eventId());
		assertEquals(updated.id(), event.aggregateId());
		assertEquals(date, event.occurredAt());
	}

	@Test
	void shouldChangeUsername() throws ValueObjectException {
		User original = createUser();

		Username newUsername = Username.create("newuserx");
		Date date = Date.create(NEXT_UPDATE);

		User updated = original.changeUsername(
				newUsername,
				UUID.randomUUID(),
				date
		);

		assertEquals(newUsername, updated.username());
		assertEquals(date, updated.updatedAt());
	}

	@Test
	void shouldNotMutateOriginalUserWhenChangingUsername() throws ValueObjectException {
		User original = createUser();

		Username originalUsername = original.username();

		User updated = original.changeUsername(
				Username.create("newuserx"),
				UUID.randomUUID(),
				Date.create(NEXT_UPDATE)
		);

		assertEquals(originalUsername, original.username());
		assertNotEquals(original.username(), updated.username());
	}

	@Test
	void shouldCreateChangeUsernameDomainEvent() throws ValueObjectException {
		UUID eventId = UUID.randomUUID();
		Date date = Date.create(NEXT_UPDATE);

		User updated = createUser().changeUsername(
				Username.create("newuserx"),
				eventId,
				date
		);

		assertEquals(1, updated.allDomainEvents().size());

		DomainEvent event = updated.allDomainEvents().getFirst();

		assertInstanceOf(ChangeUsernameEvent.class, event);
		assertEquals(eventId, event.eventId());
		assertEquals(updated.id(), event.aggregateId());
		assertEquals(date, event.occurredAt());
	}

	@Test
	void shouldBanUser() throws ValueObjectException {
		User original = createUser();

		Date date = Date.create(NEXT_UPDATE);

		User banned = original.ban(
				"Violation of terms",
				UUID.randomUUID(),
				date
		);

		assertEquals("banned", banned.status().value());
		assertEquals(date, banned.updatedAt());
	}

	@Test
	void shouldNotMutateOriginalUserWhenBanning() throws ValueObjectException {
		User original = createUser();

		User banned = original.ban(
				"Violation of terms",
				UUID.randomUUID(),
				Date.create(NEXT_UPDATE)
		);

		assertEquals("active", original.status().value());
		assertEquals("banned", banned.status().value());
	}

	@Test
	void shouldCreateBanUserDomainEvent() throws ValueObjectException {
		UUID eventId = UUID.randomUUID();
		Date date = Date.create(NEXT_UPDATE);

		User banned = createUser().ban(
				"Violation of terms",
				eventId,
				date
		);

		assertEquals(1, banned.allDomainEvents().size());

		DomainEvent event = banned.allDomainEvents().getFirst();

		assertInstanceOf(BanUserEvent.class, event);
		assertEquals(eventId, event.eventId());
		assertEquals(banned.id(), event.aggregateId());
	}

	@Test
	void shouldSuspendUser() throws ValueObjectException {
		User original = createUser();

		Date date = Date.create(NEXT_UPDATE);

		User suspended = original.suspend(
				"Suspicious activity",
				UUID.randomUUID(),
				date
		);

		assertEquals("suspended", suspended.status().value());
		assertEquals(date, suspended.updatedAt());
	}

	@Test
	void shouldNotMutateOriginalUserWhenSuspending() throws ValueObjectException {
		User original = createUser();

		User suspended = original.suspend(
				"Suspicious activity",
				UUID.randomUUID(),
				Date.create(NEXT_UPDATE)
		);

		assertEquals("active", original.status().value());
		assertEquals("suspended", suspended.status().value());
	}

	@Test
	void shouldCreateSuspendUserDomainEvent() throws ValueObjectException {
		UUID eventId = UUID.randomUUID();
		Date date = Date.create(NEXT_UPDATE);

		User suspended = createUser().suspend(
				"Suspicious activity",
				eventId,
				date
		);

		assertEquals(1, suspended.allDomainEvents().size());

		DomainEvent event = suspended.allDomainEvents().getFirst();

		assertInstanceOf(SuspendUserEvent.class, event);
		assertEquals(eventId, event.eventId());
		assertEquals(suspended.id(), event.aggregateId());
		assertEquals("Suspicious activity", event.reason());
		assertEquals(date, event.occurredAt());
	}

	@Test
	void shouldActivateUser() throws ValueObjectException {
		User original = createUser()
				.suspend(
						"Temporary suspension",
						UUID.randomUUID(),
						Date.create(NEXT_UPDATE)
				);

		User activated = original.activate(
				"Suspension removed",
				UUID.randomUUID(),
				Date.create("2026-12-03T00:00:00Z")
		);

		assertEquals("active", activated.status().value());
	}

	@Test
	void shouldNotMutateOriginalUserWhenActivating() throws ValueObjectException {
		User original = createUser()
				.suspend(
						"Temporary suspension",
						UUID.randomUUID(),
						Date.create(NEXT_UPDATE)
				);

		User activated = original.activate(
				"Suspension removed",
				UUID.randomUUID(),
				Date.create("2026-12-03T00:00:00Z")
		);

		assertEquals("suspended", original.status().value());
		assertEquals("active", activated.status().value());
	}

	@Test
	void shouldCreateActivateUserDomainEvent() throws ValueObjectException {
		UUID eventId = UUID.randomUUID();
		Date date = Date.create(NEXT_UPDATE);

		User activated = createUser().activate(
				"Activation",
				eventId,
				date
		);

		assertEquals(1, activated.allDomainEvents().size());

		DomainEvent event = activated.allDomainEvents().getFirst();

		assertInstanceOf(ActivateUserEvent.class, event);
		assertEquals(eventId, event.eventId());
		assertEquals(activated.id(), event.aggregateId());
		assertEquals("Activation", event.reason());
		assertEquals(date, event.occurredAt());
	}

	@Test
	void shouldDeactivateUser() throws ValueObjectException {
		User original = createUser();

		Date date = Date.create(NEXT_UPDATE);

		User deactivated = original.deactivate(
				"Account closed",
				UUID.randomUUID(),
				date
		);

		assertEquals("deactivated", deactivated.status().value());
		assertEquals(date, deactivated.updatedAt());
	}

	@Test
	void shouldNotMutateOriginalUserWhenDeactivating() throws ValueObjectException {
		User original = createUser();

		User deactivated = original.deactivate(
				"Account closed",
				UUID.randomUUID(),
				Date.create(NEXT_UPDATE)
		);

		assertEquals("active", original.status().value());
		assertEquals("deactivated", deactivated.status().value());
	}

	@Test
	void shouldCreateDeactivateUserDomainEvent() throws ValueObjectException {
		UUID eventId = UUID.randomUUID();
		Date date = Date.create(NEXT_UPDATE);

		User deactivated = createUser().deactivate(
				"Account closed",
				eventId,
				date
		);

		assertEquals(1, deactivated.allDomainEvents().size());

		DomainEvent event = deactivated.allDomainEvents().getFirst();

		assertInstanceOf(DeactivateUserEvent.class, event);
		assertEquals(eventId, event.eventId());
		assertEquals(deactivated.id(), event.aggregateId());
		assertEquals("Account closed", event.reason());
		assertEquals(date, event.occurredAt());
	}

	@Test
	void shouldConsiderUsersWithSameIdEquals() throws ValueObjectException {
		UUID id = UUID.randomUUID();

		User first = restoreUser(id);

		User second = User.restore(
				id,
				Name.create("Another Name"),
				Username.create("anotherx"),
				UserStatus.create("banned"),
				Email.create("second@email.com"),
				PhoneNumber.create("21 97981-8980"),
				HashedPassword.create("another-hash"),
				null,
				true,
				true,
				Date.create("2026-12-02T00:00:00Z"),
				Date.create("2026-12-02T00:00:00Z")
		);

		assertEquals(first, second);
	}

	@Test
	void shouldConsiderUsersWithDifferentIdNotEquals() throws ValueObjectException {
		User first = createUser();
		User second = createUser();

		assertNotEquals(first, second);
	}

	@Test
	void shouldConsiderSameObjectEquals() throws ValueObjectException {
		User user = createUser();

		assertEquals(user, user);
	}

	@Test
	void shouldNotConsiderNullEquals() throws ValueObjectException {
		User user = createUser();

		assertNotEquals(null, user);
	}

	@Test
	void shouldNotConsiderDifferentTypeEquals() throws ValueObjectException {
		User user = createUser();

		assertNotEquals("user", user);
	}

	@Test
	void shouldHaveSameHashCodeWhenUsersHaveSameId() throws ValueObjectException {
		UUID id = UUID.randomUUID();

		User first = restoreUser(id);

		User second = User.restore(
				id,
				Name.create("Another Name"),
				Username.create("anotherx"),
				UserStatus.create("banned"),
				Email.create("second@email.com"),
				PhoneNumber.create("21 979818980"),
				HashedPassword.create("another-hash"),
				null,
				true,
				true,
				Date.create("2026-12-02T00:00:00Z"),
				Date.create("2026-12-02T00:00:00Z")
		);

		assertEquals(first, second);
		assertEquals(first.hashCode(), second.hashCode());
	}

	@Test
	void shouldPreserveUserIdWhenChangingEmail() throws ValueObjectException {
		User original = createUser();

		User updated = original.changeEmail(
				Email.create("new@email.com"),
				UUID.randomUUID(),
				Date.create(NEXT_UPDATE)
		);

		assertEquals(original.id(), updated.id());
	}

	@Test
	void shouldPreserveUserIdWhenChangingPassword() throws ValueObjectException {
		User original = createUser();

		User updated = original.changePassword(
				HashedPassword.create("new-hash"),
				UUID.randomUUID(),
				Date.create(NEXT_UPDATE)
		);

		assertEquals(original.id(), updated.id());
	}

	@Test
	void shouldPreserveUserIdWhenChangingUsername() throws ValueObjectException {
		User original = createUser();

		User updated = original.changeUsername(
				Username.create("newuserx"),
				UUID.randomUUID(),
				Date.create(NEXT_UPDATE)
		);

		assertEquals(original.id(), updated.id());
	}

	@Test
	void shouldPreserveUserIdWhenChangingPhoneNumber() throws ValueObjectException {
		User original = createUser();

		User updated = original.changePhoneNumber(
				PhoneNumber.create("21979818980"),
				UUID.randomUUID(),
				Date.create(NEXT_UPDATE)
		);

		assertEquals(original.id(), updated.id());
	}

	@Test
	void shouldPreserveUserIdWhenChangingStatus() throws ValueObjectException {
		User original = createUser();

		User banned = original.ban(
				"Violation",
				UUID.randomUUID(),
				Date.create(NEXT_UPDATE)
		);

		assertEquals(original.id(), banned.id());
	}
}