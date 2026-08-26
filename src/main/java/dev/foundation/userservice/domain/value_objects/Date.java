package dev.foundation.userservice.domain.value_objects;

import dev.foundation.userservice.domain.pattern.DatePattern;
import dev.foundation.userservice.errors.ValueObjectException;

import java.time.Instant;
import java.time.format.DateTimeParseException;

public class Date {
    private final Instant date;
    private Date(Instant date){
        this.date = date;
    }

    public static Date create(String date) throws ValueObjectException {
        if(date == null || date.isEmpty()){
            throw new ValueObjectException("There's an error on date verification. Please, try a date.", Date.class.getSimpleName(), "Invalid arguments");
        }

		if(!DatePattern.PATTERN.matcher(date).matches()){
			throw new ValueObjectException("There's an error on date verification. Please, try a date.", Date.class.getSimpleName(), "Invalid arguments");
		}

	    if (date.contains("T24:")) {
		    throw new ValueObjectException("Invalid time. Hours must be between 00 and 23.",Date.class.getSimpleName(), "Invalid arguments");
	    }

        try{
            Instant dateInstant = Instant.parse(date);
            return new Date(dateInstant);
        }
        catch (DateTimeParseException e){
            throw new ValueObjectException(e.getMessage(), Date.class.getSimpleName(), "Invalid arguments");
        }

    }

    public Instant value(){
        return this.date;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Date dateVo))return false;
        return this.date.equals(dateVo.value());
    }

    @Override
    public int hashCode(){
        return this.date.hashCode();
    }
}
