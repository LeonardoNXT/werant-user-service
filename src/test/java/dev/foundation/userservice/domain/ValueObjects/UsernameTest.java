package dev.foundation.userservice.domain.ValueObjects;

import dev.foundation.userservice.domain.value_objects.Username;
import dev.foundation.userservice.errors.ValueObjectException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class UsernameTest {
	@Test
	void shouldConsiderUsernameWithSameValueEquals() throws ValueObjectException {
		Username first = Username.create("NXT Y YTNIF");
		Username second = Username.create("NXT Y YTNIF");

		assertEquals(first, second);
	}

	@Test
	void shouldConsiderUsernameWithDifferentValueEquals() throws ValueObjectException {
		Username first = Username.create("NXT Y YTNIF");
		Username second = Username.create("nxt y ytnif");

		assertNotEquals(first, second);
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"Leo\u0000nardo",
			"Leonardo\nLeal",
			"Leonardo123",
			"Leonardo_Leal",
			"Leonardo@Leal",
			"L",
			"a",
			"Le",
			"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
			"L.",
			"-Leonardo",
			"Leonardo-",
			"Leonardo  Leal"
	})
	void shouldConsiderInvalidUsernames(String username){
		assertThrows(ValueObjectException.class, () -> {
			Username.create(username);
		});
	}
}
