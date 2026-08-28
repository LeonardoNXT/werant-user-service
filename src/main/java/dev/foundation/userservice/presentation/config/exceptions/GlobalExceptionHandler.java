package dev.foundation.userservice.presentation.config.exceptions;

import dev.foundation.userservice.errors.ApplicationException;
import dev.foundation.userservice.errors.InfrastructureException;
import dev.foundation.userservice.errors.ValueObjectException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

	@ExceptionHandler(Exception.class)
	public ResponseEntity<RecordExceptionMessage> handleException(Exception e){
		return exception(500, e.getMessage(), e.getClass().getSimpleName(), "UNKNOWN");
	}

	private ResponseEntity<RecordExceptionMessage> exception(int code, String message, String locale, String violation){
		return ResponseEntity.status(code).body(new RecordExceptionMessage(message,code, locale, violation));
	}
}
