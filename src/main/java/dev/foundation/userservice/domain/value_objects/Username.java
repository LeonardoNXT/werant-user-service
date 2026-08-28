package dev.foundation.userservice.domain.value_objects;

import dev.foundation.userservice.domain.pattern.UsernamePattern;
import dev.foundation.userservice.errors.ValueObjectException;

public class Username {
    private final String username;

    private Username(String username){
        this.username = username;
    }

    public static Username create(String username) throws ValueObjectException {
	    if(username == null || username.isEmpty()){
		    throw new ValueObjectException("The username typed is invalid. The username name can't be null or empty.", Username.class.getSimpleName(), "Invalid Argument");
	    }

		String trimmedUsername = username.trim();

		if(trimmedUsername.length() < 3 || trimmedUsername.length() > 20){
			throw new ValueObjectException("The username typed is invalid. Try again with more than 3 characters do proceed.", Username.class.getSimpleName(), "Invalid Argument");
		}

        if(!UsernamePattern.PATTERN.matcher(trimmedUsername).matches()){
            throw new ValueObjectException("The username typed is invalid. Try again without special characters.", Username.class.getSimpleName(), "Invalid Argument");
        }



        return new Username(trimmedUsername);
    }

    public String value(){
        return this.username;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Username usernameVo))return false;
        return this.username.equals(usernameVo.value());
    }

    @Override
    public int hashCode(){
        return this.username.hashCode();
    }
}
