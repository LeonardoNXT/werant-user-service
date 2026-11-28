package dev.foundation.userservice.domain.entities;

import dev.foundation.userservice.domain.aggregate.AggregateRoot;
import dev.foundation.userservice.domain.events.*;
import dev.foundation.userservice.domain.value_objects.*;
import dev.foundation.userservice.errors.ValueObjectException;

import java.util.UUID;

public class User extends AggregateRoot {
    private final UUID id;
    private final Name name;
    private final Username username;
    private final UserStatus status;
    private final UserVersion version;
    private final Email email;
    private final PhoneNumber phoneNumber;
    private final HashedPassword password;
    private final UUID profilePictureId;
    private final boolean emailVerified;
    private final boolean numberVerified;
    private final Date createdAt;
    private final Date updatedAt;

    private User(
        UUID id,
        Name name,
        Username username,
        Email email,
        UserVersion version,
        PhoneNumber phoneNumber,
        UserStatus status,
        HashedPassword password,
        UUID profilePictureId,
        boolean emailVerified,
        boolean numberVerified,
        Date createdAt,
        Date updatedAt
    ) {
        super();
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.version = version;
        this.name = name;
        this.username = username;
        this.password = password;
        this.profilePictureId = profilePictureId;
        this.emailVerified = emailVerified;
        this.numberVerified = numberVerified;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User create(
        UUID id,
        Name name,
        Username username,
        Email email,
        PhoneNumber phoneNumber,
        HashedPassword password,
        UUID profilePictureId,
        boolean emailVerified,
        boolean numberVerified,
        Date createdAt,
        Date updatedAt,
        UUID eventId
    ) throws ValueObjectException {
        User user = new User(
            id,
            name,
            username,
            email,
            UserVersion.create(0),
            phoneNumber,
            UserStatus.create("active"),
            password,
            profilePictureId,
            emailVerified,
            numberVerified,
            createdAt,
            updatedAt
        );
        user.addDomainEvents(CreateUserEvent.create(eventId, user.id(), createdAt));
        return user;
    }

    public static User restore(
        UUID id,
        Name name,
        Username username,
        UserStatus userStatus,
        Email email,
        UserVersion version,
        PhoneNumber phoneNumber,
        HashedPassword password,
        UUID profilePictureId,
        boolean emailVerified,
        boolean numberVerified,
        Date createdAt,
        Date updatedAt
    ) {
        return new User(
            id,
            name,
            username,
            email,
            version,
            phoneNumber,
            userStatus,
            password,
            profilePictureId,
            emailVerified,
            numberVerified,
            createdAt,
            updatedAt
        );
    }

    public User changeEmail(Email email, UUID eventId, Date date) {
        User user = new User(
            this.id,
            this.name,
            this.username,
            email,
            this.version,
            this.phoneNumber,
            this.status,
            this.password,
            this.profilePictureId,
            this.emailVerified,
            this.numberVerified,
            this.createdAt,
            date
        );
        user.addDomainEvents(ChangeEmailEvent.create(eventId, user.id(), date));
        return user;
    }

    public User changePhoneNumber(PhoneNumber phoneNumber, UUID eventId, Date date) {
        User user = new User(
            this.id,
            this.name,
            this.username,
            this.email,
            this.version,
            phoneNumber,
            this.status,
            this.password,
            this.profilePictureId,
            this.emailVerified,
            this.numberVerified,
            this.createdAt,
            date
        );
        user.addDomainEvents(ChangePhoneNumberEvent.create(eventId, user.id(), date));
        return user;
    }

    public User changePassword(HashedPassword password, UUID eventId, Date date) {
        User user = new User(
            this.id,
            this.name,
            this.username,
            this.email,
            this.version,
            this.phoneNumber,
            this.status,
            password,
            this.profilePictureId,
            this.emailVerified,
            this.numberVerified,
            this.createdAt,
            date
        );
        user.addDomainEvents(ChangePasswordEvent.create(eventId, user.id(), date));
        return user;
    }

    public User changeUsername(Username username, UUID eventId, Date date) {
        User user = new User(
            this.id,
            this.name,
            username,
            this.email,
            this.version,
            this.phoneNumber,
            this.status,
            this.password,
            this.profilePictureId,
            this.emailVerified,
            this.numberVerified,
            this.createdAt,
            date
        );
        user.addDomainEvents(ChangeUsernameEvent.create(eventId, user.id(), date));
        return user;
    }

    public User ban(String reason, UUID eventId, Date date) throws ValueObjectException {
        User user = new User(
            this.id,
            this.name,
            this.username,
            this.email,
            this.version,
            this.phoneNumber,
            UserStatus.create("banned"),
            this.password,
            this.profilePictureId,
            emailVerified,
            numberVerified,
            createdAt,
            date
        );
        user.addDomainEvents(BanUserEvent.create(eventId, reason, user.id(), date));
        return user;
    }

    public User suspend(String reason, UUID eventId, Date date) throws ValueObjectException {
        User user = new User(
            this.id,
            this.name,
            this.username,
            this.email,
            this.version,
            this.phoneNumber,
            UserStatus.create("suspended"),
            this.password,
            this.profilePictureId,
            emailVerified,
            numberVerified,
            createdAt,
            date
        );
        user.addDomainEvents(SuspendUserEvent.create(eventId, reason, user.id(), date));
        return user;
    }

    public User activate(String reason, UUID eventId, Date date) throws ValueObjectException {
        User user = new User(
            this.id,
            this.name,
            this.username,
            this.email,
            this.version,
            this.phoneNumber,
            UserStatus.create("active"),
            this.password,
            this.profilePictureId,
            emailVerified,
            numberVerified,
            createdAt,
            date
        );
        user.addDomainEvents(ActivateUserEvent.create(eventId, reason, user.id(), date));
        return user;
    }

    public User deactivate(String reason, UUID eventId, Date date) throws ValueObjectException {
        User user = new User(
            this.id,
            this.name,
            this.username,
            this.email,
            this.version,
            this.phoneNumber,
            UserStatus.create("deactivated"),
            this.password,
            this.profilePictureId,
            emailVerified,
            numberVerified,
            createdAt,
            date
        );
        user.addDomainEvents(DeactivateUserEvent.create(eventId, reason, user.id(), date));
        return user;
    }

    public UUID id() {
        return this.id;
    }

    public Name name() {
        return this.name;
    }

    public Username username() {
        return this.username;
    }

    public UserVersion version() {
        return this.version;
    }

    public UserStatus status() {
        return this.status;
    }

    public Email email() {
        return this.email;
    }

    public PhoneNumber phoneNumber() {
        return this.phoneNumber;
    }

    public HashedPassword password() {
        return this.password;
    }

    public UUID profilePictureId() {
        return this.profilePictureId;
    }

    public boolean emailVerified() {
        return this.emailVerified;
    }

    public boolean numberVerified() {
        return this.numberVerified;
    }

    public Date createdAt() {
        return this.createdAt;
    }

    public Date updatedAt() {
        return this.updatedAt;
    }

    @Override
    public boolean equals(Object u) {
        if (u == this)
            return true;
        if (!(u instanceof User user))
            return false;
        return this.id().equals(user.id());
    }

    @Override
    public int hashCode() {
        return this.id().hashCode();
    }
}
