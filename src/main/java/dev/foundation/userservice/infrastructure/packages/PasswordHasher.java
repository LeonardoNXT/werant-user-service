package dev.foundation.userservice.infrastructure.packages;

import dev.foundation.userservice.application.ports.PasswordHasherPort;
import dev.foundation.userservice.domain.value_objects.HashedPassword;
import dev.foundation.userservice.domain.value_objects.Password;
import dev.foundation.userservice.errors.InfrastructureException;
import dev.foundation.userservice.errors.SystemException;
import dev.foundation.userservice.errors.ValueObjectException;
import dev.foundation.userservice.errors.types.ExceptionType;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordHasher implements PasswordHasherPort {
	private final PasswordEncoder encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
	@Override
	public HashedPassword hash(Password password) throws SystemException {
		try{
			String hash = this.encoder.encode(password.value());
			return HashedPassword.create(hash);
		}
		catch (Exception e){
			if(e instanceof ValueObjectException valueObjectException){
				throw valueObjectException.at(PasswordHasher.class.getSimpleName());
			}
			throw new InfrastructureException(e.getMessage(),PasswordHasher.class.getSimpleName(), "UNKNOWN", ExceptionType.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public boolean verify(Password password, HashedPassword hashedPassword){
		return encoder.matches(password.value(), hashedPassword.value());
	}
}
