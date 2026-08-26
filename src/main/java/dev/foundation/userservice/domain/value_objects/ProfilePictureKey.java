package dev.foundation.userservice.domain.value_objects;

import dev.foundation.userservice.domain.pattern.ProfilePictureKeyPattern;
import dev.foundation.userservice.errors.ValueObjectException;

public class ProfilePictureKey {
    private final String key;

    private ProfilePictureKey(String key){
        this.key = key;
    }

    public static ProfilePictureKey create(String key) throws ValueObjectException {
        if(!ProfilePictureKeyPattern.PATTERN.matcher(key).matches()){
            throw new ValueObjectException("The key offered is invalid. Please, try a new one to proceed.", ProfilePictureKey.class.getSimpleName(), "Invalid Argument");
        }
        return new ProfilePictureKey(key);
    }

    public String value(){
        return this.key;
    }
}
