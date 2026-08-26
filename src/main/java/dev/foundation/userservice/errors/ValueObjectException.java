package dev.foundation.userservice.errors;

import dev.foundation.userservice.errors.types.ExceptionType;
import dev.foundation.userservice.errors.types.ExceptionModuleEnum;

public class ValueObjectException extends SystemException {
    public ValueObjectException(String message, String locale,String violation){
        super(message, ExceptionModuleEnum.VALUE_OBJECT.toString().toLowerCase(), locale, violation, ExceptionType.INVALID_ARGUMENT);
    }

	public ValueObjectException at(String locale){
		return new ValueObjectException(this.getMessage(),locale,this.violation);
	}
}
