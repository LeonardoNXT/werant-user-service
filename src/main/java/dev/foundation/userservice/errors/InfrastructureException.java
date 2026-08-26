package dev.foundation.userservice.errors;

import dev.foundation.userservice.errors.types.ExceptionModuleEnum;

public class InfrastructureException extends SystemException {
    public InfrastructureException(String message, String locale, String violation, int type){
        super(message, ExceptionModuleEnum.INFRASTRUCTURE.toString().toLowerCase(), locale, violation, type);
    }
}
