package dev.foundation.userservice.domain.valueObjects;

import dev.foundation.userservice.domain.value_objects.Name;
import dev.foundation.userservice.errors.ValueObjectException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class NameTest {
	@Test
	void shouldConsiderNameWithSameValueEquals() throws ValueObjectException {
		Name first = Name.create("Leonardo Leal");
		Name second = Name.create("Leonardo Leal");

		assertEquals(first, second);
	}

	@Test
	void shouldConsiderNameWithDifferentValueEquals() throws ValueObjectException {
		Name first = Name.create("Leonardo Leal");
		Name second = Name.create("Giovanni Alencar");

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
	void shouldConsiderInvalidUsernames(String name){
		assertThrows(ValueObjectException.class, () -> {
			Name.create(name);
		});
	}
}
