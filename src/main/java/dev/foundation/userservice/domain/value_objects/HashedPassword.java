package dev.foundation.userservice.domain.value_objects;

import dev.foundation.userservice.errors.ValueObjectException;

public class HashedPassword {
    private final String hashedPassword;

    private HashedPassword(String hashedPassword){
        this.hashedPassword = hashedPassword;
    }

    public static HashedPassword create(String hashedPassword) throws ValueObjectException {
        if(hashedPassword == null || hashedPassword.isEmpty()){
            throw new ValueObjectException("The hashed password can't be empty", HashedPassword.class.getSimpleName(), "Invalid arguments");
        }

		return new HashedPassword(hashedPassword);
    }

    public String value(){
        return this.hashedPassword;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof HashedPassword hashedPasswordVo))return false;
        return this.hashedPassword.equals(hashedPasswordVo.value());
    }

    @Override
    public int hashCode(){
        return this.hashedPassword.hashCode();
    }
}
