package dev.foundation.userservice.domain.value_objects;

import dev.foundation.userservice.domain.pattern.NamePattern;
import dev.foundation.userservice.errors.ValueObjectException;

import java.util.regex.Pattern;

public class Name {
    private final String name;

    private Name(String name){
        this.name = name;
    }

    public static Name create(String name) throws ValueObjectException {
	    if (name == null || name.isBlank()) {
		    throw exception("The name is required and cannot be empty.");
	    }

		String trimmedName = name.trim();

		if(trimmedName.length() < 8 || trimmedName.length() > 100){
			throw exception("The name cannot be accepted because it does not meet the length requirement.");
	    }

        if(!NamePattern.PATTERN.matcher(trimmedName).matches()){
	        throw exception("The name can't be accepted because the structure is wrong.");
        }

        return new Name(trimmedName);
    }

	private static ValueObjectException exception(String message){
		return new ValueObjectException(message,Name.class.getSimpleName(), "Invalid Argument");
	}

    public String value(){
        return this.name;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Name nameVo))return false;
        return this.name.equals(nameVo.value());
    }

    @Override
    public int hashCode(){
        return this.name.hashCode();
    }


}
