package dev.foundation.userservice.presentation.config.exceptions;

import dev.foundation.userservice.errors.ApplicationException;
import dev.foundation.userservice.errors.InfrastructureException;
import dev.foundation.userservice.errors.SystemException;
import dev.foundation.userservice.errors.ValueObjectException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ValueObjectException.class)
	public ResponseEntity<RecordExceptionMessage> handleValueObjectException(ValueObjectException e){
		return exception(e.code,e.getMessage(),e.locale,e.violation);
	}

	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<RecordExceptionMessage> handleApplicationException(ApplicationException e){
		return exception(e.code,e.getMessage(),e.locale,e.violation);
	}

	@ExceptionHandler(InfrastructureException.class)
	public ResponseEntity<RecordExceptionMessage> handleInfrastructureException(InfrastructureException e){
		return exception(e.code,e.getMessage(),e.locale,e.violation);
	}

	@ExceptionHandler(SystemException.class)
	public ResponseEntity<RecordExceptionMessage> handleSystemException(SystemException e){
		return exception(e.code,e.getMessage(),e.locale,e.violation);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<RecordExceptionMessage> handleValidationException(MethodArgumentNotValidException e){
		String message = e.getBindingResult().getFieldErrors().stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage)
				.filter(Objects::nonNull)
				.findFirst()
				.orElse("Invalid request body.");

		return exception(400, message, "RequestRegisterUserDto", "INVALID ARGUMENT");
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<RecordExceptionMessage> handleUnreadableMessage(){
		return exception(400, "Invalid request body.", "request", "INVALID ARGUMENT");
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<RecordExceptionMessage> handleException(){
		return exception(500, "Internal server error.", "server", "INTERNAL SERVER ERROR");
	}

	private ResponseEntity<RecordExceptionMessage> exception(int code, String message, String locale, String violation){
		if (code >= 500) {
			return ResponseEntity.status(code).body(
					new RecordExceptionMessage(
							"Internal server error.",
							code,
							"server",
							"INTERNAL SERVER ERROR"
					)
			);
		}

		return ResponseEntity.status(code).body(new RecordExceptionMessage(message,code, locale, violation));
	}
}
