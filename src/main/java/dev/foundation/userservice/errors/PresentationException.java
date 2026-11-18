package dev.foundation.userservice.errors;


import dev.foundation.userservice.errors.types.ExceptionModuleEnum;

public class PresentationException extends SystemException {
	public PresentationException(String message, String locale, String violation, int type){
		super(message, ExceptionModuleEnum.PRESENTATION.toString().toLowerCase(), locale, violation, type);
	}
}
