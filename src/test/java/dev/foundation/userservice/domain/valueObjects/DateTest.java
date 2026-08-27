package dev.foundation.userservice.domain.valueObjects;

import dev.foundation.userservice.domain.value_objects.Date;
import dev.foundation.userservice.errors.ValueObjectException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class DateTest {
	@Test
	void shouldConsiderWithSameValueEquals() throws ValueObjectException {
		Date date = Date.create("2026-12-01T00:00:00Z");
		assertEquals("2026-12-01T00:00:00Z", date.value().toString());
	}

	@Test
	void shouldConsiderWithDifferentValueEquals() throws ValueObjectException {
		Date first = Date.create("2026-12-01T00:00:00Z");
		Date second = Date.create("2026-11-01T00:00:00Z");
		assertNotEquals(second, first);
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"9999:99:999",
			"not-a-date",
			"null",
			"",
			" ",
			"🗿",
			"AAAAAAAAAAAAAAAAAAAA",
			"2026:12:01T00:00:00Z",
			"2026/12/01T00:00:00Z",
			"2026.12.01T00:00:00Z",
			"2026-12-01 00:00:00Z",
			"2026",
			"2026-12",
			"2026-12-01",
			"2026-12-01T",
			"2026-12-01T00:00",
			"2026-12-01T00:00:00",
			"01-12-2026T00:00:00Z",
			"12-01-2026T00:00:00Z",
			"01/12/2026T00:00:00Z",
			"2026-1-01T00:00:00Z",
			"2026-01-1T00:00:00Z",
			"26-12-01T00:00:00Z",
			"02026-12-01T00:00:00Z",
			"2026-00-01T00:00:00Z",
			"2026-13-01T00:00:00Z",
			"2026-99-01T00:00:00Z",
			"2026-123-01T00:00:00Z",
			"2026-01-00T00:00:00Z",
			"2026-01-32T00:00:00Z",
			"2026-12-32T00:00:00Z",
			"2026-04-31T00:00:00Z",
			"2026-06-31T00:00:00Z",
			"2026-09-31T00:00:00Z",
			"2026-11-31T00:00:00Z",
			"2026-02-29T00:00:00Z",
			"2026-02-30T00:00:00Z",
			"2026-02-31T00:00:00Z",
			"2026-12-01T24:00:00Z",
			"2026-12-01T25:00:00Z",
			"2026-12-01T99:00:00Z",
			"2026-12-01T00:60:00Z",
			"2026-12-01T00:99:00Z",
			"2026-12-01T00:00:60Z",
			"2026-12-01T00:00:99Z",
			"2026-12-01T00:00:00.Z",
			"2026-12-01T00:00:00.1234567890Z",
			"2026-12-01T00:00:00,123Z",
			"2026-12-01T00:00:00",
			"2026-12-01T00:00:00X",
			"2026-12-01T00:00:00+",
			"2026-12-01T00:00:00+99:99",
			"abc2026-12-01T00:00:00Z",
			"2026-12-01T00:00:00Zabc",
			" 2026-12-01T00:00:00Z",
			"2026-12-01T00:00:00Z ",
			"2026-12-01T00:00:00\nZ",
			"2026-12-01T00:00:00\tZ",
			"2026-12-01T00:00:00\u0000Z",
			"２０２６-１２-０１T００:００:００Z",
			"2026-12-01T00:00:00💀Z"
	})
	void shouldNotAcceptInvalidDate(String value) {
		assertThrows(
				ValueObjectException.class,
				() -> Date.create(value)
		);
	}

}
