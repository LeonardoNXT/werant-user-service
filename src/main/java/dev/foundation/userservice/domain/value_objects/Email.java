package dev.foundation.userservice.domain.value_objects;

import dev.foundation.userservice.domain.pattern.EmailPattern;
import dev.foundation.userservice.errors.ValueObjectException;

import java.util.Locale;

public class Email {
    private final String email;

    private Email(String email){
        this.email = email;
    }

	public static Email create(String email) throws ValueObjectException {
		if (email == null || email.isBlank()) {
			throw createException();
		}

		String trimmedEmail = email.trim().toLowerCase();

		if (!EmailPattern.PATTERN.matcher(trimmedEmail).matches()) {
			throw createException();
		}

		return new Email(trimmedEmail);
	}

	private static ValueObjectException createException() {
		return new ValueObjectException(
				"There's an error in your email. Please, try a different email.",
				Email.class.getSimpleName(),
				"Invalid arguments"
		);
	}


    public String value(){
        return this.email;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Email emailVo))return false;
        return this.email.equals(emailVo.value());
    }

    @Override
    public int hashCode(){
        return this.email.hashCode();
    }

}
