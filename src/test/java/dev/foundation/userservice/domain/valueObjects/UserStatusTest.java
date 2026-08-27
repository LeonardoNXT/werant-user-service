package dev.foundation.userservice.domain.valueObjects;

import dev.foundation.userservice.domain.value_objects.UserStatus;
import dev.foundation.userservice.errors.ValueObjectException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class UserStatusTest {

	@ParameterizedTest(name = "[{index}] Must create a valid status: {0}")
	@ValueSource(strings = {
			"active", "deactivated", "banned", "suspended",
			"ACTIVE", "  Active  ", "sUsPeNdEd", " BANNED\n"
	})
	void shouldCreateValidUserStatus(String validStatus) throws ValueObjectException {
		UserStatus status = UserStatus.create(validStatus);

		assertNotNull(status);
		assertEquals(validStatus.trim().toLowerCase(), status.value());
	}

	@ParameterizedTest(name = "[{index}] Must reject a invalid status {0}")
	@MethodSource("provideInvalidStatuses")
	void shouldThrowExceptionForInvalidStatuses(String invalidStatus) {
		assertThrows(ValueObjectException.class, () -> {
			UserStatus.create(invalidStatus);
		});
	}

	@Test
	void shouldConsiderStatusWithSameValueEquals() throws ValueObjectException {
		UserStatus first = UserStatus.create("active");
		UserStatus second = UserStatus.create(" ACTIVE "); // Entrada bagunçada, mas resultado deve ser igual

		assertEquals(first, second);
		assertEquals(first.hashCode(), second.hashCode());
	}

	@Test
	void shouldNotConsiderStatusWithDifferentValueEquals() throws ValueObjectException {
		UserStatus first = UserStatus.create("active");
		UserStatus second = UserStatus.create("suspended");

		assertNotEquals(first, second);
	}

	// --- Provider de Dados Caóticos ---

	private static Stream<String> provideInvalidStatuses() {
		return Stream.of(
				null,
				"",
				" ",
				"   \n \t  ",
				"deleted",
				"admin",
				"123",
				"🗿",
				"act ive",
				"active_"
		);
	}
}