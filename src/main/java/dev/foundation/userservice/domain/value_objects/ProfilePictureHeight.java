package dev.foundation.userservice.domain.value_objects;

import dev.foundation.userservice.domain.pattern.ProfilePictureMaxHeightPattern;
import dev.foundation.userservice.errors.ValueObjectException;

import javax.swing.*;

public class ProfilePictureHeight {
    private final int height;
    private ProfilePictureHeight(int height){
        this.height = height;
    }

    public static ProfilePictureHeight create(int height) throws ValueObjectException {
        if(height > ProfilePictureMaxHeightPattern.PATTERN){
            throw new ValueObjectException("The height exceeds the allowed limit. Please, try a new image those size is within the limits.", ProfilePictureHeight.class.getSimpleName(), "Invalid Argument");
        }

        return new ProfilePictureHeight(height);
    }

    public int value(){
        return this.height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return false;
        if (!(o instanceof ProfilePictureHeight profilePictureHeight)) return false;
        return this.height == profilePictureHeight.height;
    }

}
