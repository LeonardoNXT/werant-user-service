package dev.foundation.userservice.application.usecases;

import dev.foundation.userservice.errors.SystemException;

public interface Usecase <I,R>{
    R execute(I input) throws SystemException;
}
