package dev.foundation.userservice.domain.value_objects;

import dev.foundation.userservice.domain.pattern.ProfilePictureMaxHeightPattern;
import dev.foundation.userservice.domain.pattern.ProfilePictureMaxWidthPattern;
import dev.foundation.userservice.errors.ValueObjectException;

public class ProfilePictureWidth {
    private final int width;
    private ProfilePictureWidth(int width){
        this.width = width;
    }

    public static ProfilePictureWidth create(int width) throws ValueObjectException {
        if(width > ProfilePictureMaxWidthPattern.PATTERN){
            throw new ValueObjectException("The height exceeds the allowed limit. Please, try a new image those size is within the limits.", ProfilePictureHeight.class.getSimpleName(), "Invalid Argument");
        }

        return new ProfilePictureWidth(width);
    }

    public int value(){
        return this.width;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return false;
        if (!(o instanceof ProfilePictureWidth profilePictureWidth)) return false;
        return this.width == profilePictureWidth.width;
    }
}
