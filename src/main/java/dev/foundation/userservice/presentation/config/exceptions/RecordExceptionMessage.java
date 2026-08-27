package dev.foundation.userservice.presentation.config.exceptions;

public record RecordExceptionMessage(
		String message,
		int code,
		String locale,
		String violation
) {
}
