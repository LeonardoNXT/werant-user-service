package dev.foundation.userservice.errors;

import dev.foundation.userservice.errors.types.ExceptionModuleEnum;

public class ApplicationException extends SystemException {
    public ApplicationException(String message, String locale, String violation, int type){
        super(message, ExceptionModuleEnum.APPLICATION.toString().toLowerCase(), locale, violation, type);
    }
}
