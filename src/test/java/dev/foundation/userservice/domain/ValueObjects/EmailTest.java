package dev.foundation.userservice.domain.ValueObjects;

import dev.foundation.userservice.domain.value_objects.Email;
import dev.foundation.userservice.errors.ValueObjectException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;


public class EmailTest {
	@Test
	void shouldConsiderEmailsWithSameValueEquals() throws ValueObjectException {
		Email first = Email.create("example@gmail.com");
		Email second = Email.create("example@gmail.com");
		assertEquals(first, second);
	}

	@Test
	void shouldNotConsiderEmailsWithDifferentValueEquals() throws ValueObjectException {
		Email first = Email.create("example.test@gmail.com");
		Email second = Email.create("example@gmail.com");

		assertNotEquals(first,second);
	}

	@Test
	void shouldConsiderReturnWithSameValue() throws ValueObjectException {
		Email email = Email.create("example.test@gmail.com");
		assertEquals("example.test@gmail.com", email.value());
	}

	@Test
	void shouldNotConsiderReturnWithDifferentValue() throws ValueObjectException{
		Email email = Email.create("example.test@gmail.com");
		assertNotEquals("example@gmail.com", email);
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"leo\u0000nardo@gmail.com",
			"leonardo@gma\\il.com",
			"leo nardo@gmail.com",
			"leonardo@gmail..com",
			"leonardo.@gmail.com",
			".leonardo@gmail.com",
			"leonardo@://-domain.com",
			"leonardo@gmail.c",
			"\"leonardo@gmail.com",
			"leonardo(comentario)@gmail.com",
			"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@gmail.com",
			"leonardo@aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.com"
	})
	void shouldNotConsiderInvalidEmailFirst(String email) throws ValueObjectException {
		assertThrows(ValueObjectException.class, () -> {
			Email.create(email);
		});
	}

}
