package dev.foundation.userservice.domain.ValueObjects;

import dev.foundation.userservice.domain.value_objects.Password;
import dev.foundation.userservice.errors.ValueObjectException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordTest {

	@ParameterizedTest(name = "[{index}] Must create a valid password: {0}")
	@ValueSource(strings = {
			"Password123!",
			"Valid1234",
			"123456aB",
			"SUPERsecret99$",
			"Ab1*&%$#@!?"
	})
	void shouldCreateValidPassword(String validPassword) throws ValueObjectException {
		Password password = Password.create(validPassword);

		assertNotNull(password);
		assertEquals(validPassword, password.value());
	}

	@ParameterizedTest(name = "[{index}] Must reject invalid passoword: {0}")
	@MethodSource("provideInvalidPasswords")
	void shouldThrowExceptionForInvalidPasswords(String invalidPassword) {
		assertThrows(ValueObjectException.class, () -> {
			Password.create(invalidPassword);
		});
	}

	@Test
	void shouldConsiderPasswordWithSameValueEquals() throws ValueObjectException {
		Password first = Password.create("StrongPass1!");
		Password second = Password.create("StrongPass1!");

		assertEquals(first, second);
		assertEquals(first.hashCode(), second.hashCode());
	}

	@Test
	void shouldNotConsiderPasswordWithDifferentValueEquals() throws ValueObjectException {
		Password first = Password.create("StrongPass1!");
		Password second = Password.create("AnotherPass2@");

		assertNotEquals(first, second);
	}

	private static Stream<String> provideInvalidPasswords() {
		return Stream.of(
				null,
				"",
				" ",
				"short1A",
				"nouppercase1!",
				"NOLOWERCASE1!",
				"NoDigitsHere!",
				"Valid123-",
				" Valid123!",
				"Valid123! ",
				"Valid\n123!",
				"🗿Password123!"
		);
	}
}