package dev.foundation.userservice.domain.ValueObjects;

import dev.foundation.userservice.domain.value_objects.Role;
import dev.foundation.userservice.errors.ValueObjectException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class RoleTest {

	@ParameterizedTest(name = "[{index}] Must create a valid role: {0}")
	@ValueSource(strings = {
			"user", "admin", "developer", "owner",
			"ADMIN", "  Developer  ", "oWnEr", " user\n"
	})
	void shouldCreateValidRole(String validRole) throws ValueObjectException {
		Role role = Role.create(validRole);

		assertNotNull(role);
		assertEquals(validRole.trim().toLowerCase(), role.value());
	}

	@ParameterizedTest(name = "[{index}] Must reject a invalid role: {0}")
	@MethodSource("provideInvalidRoles")
	void shouldThrowExceptionForInvalidRoles(String invalidRole) {
		assertThrows(ValueObjectException.class, () -> {
			Role.create(invalidRole);
		});
	}

	@Test
	void shouldConsiderRoleWithSameValueEquals() throws ValueObjectException {
		Role first = Role.create("admin");
		Role second = Role.create(" ADMIN ");

		assertEquals(first, second);
		assertEquals(first.hashCode(), second.hashCode());
	}

	@Test
	void shouldNotConsiderRoleWithDifferentValueEquals() throws ValueObjectException {
		Role first = Role.create("user");
		Role second = Role.create("owner");

		assertNotEquals(first, second);
	}

	@Test
	void shouldReturnTrueWhenComparingWithStringValue() throws ValueObjectException {
		Role role = Role.create("developer");

		assertTrue(role.equals("developer"));
		assertTrue(role.equals(" DEVELOPER "));
	}

	@Test
	void shouldReturnFalseWhenComparingWithInvalidStringValue() throws ValueObjectException {
		Role role = Role.create("admin");

		assertFalse(role.equals("user"));
		assertFalse(role.equals(""));
		assertFalse(role.equals((String) null));
	}

	private static Stream<String> provideInvalidRoles() {
		return Stream.of(
				null,
				"",
				" ",
				"   \n \t  ",
				"guest",
				"manager",
				"123",
				"🗿",
				"ad min",
				"user_"
		);
	}
}