package dev.foundation.userservice.domain.value_objects;

import dev.foundation.userservice.domain.entities.ProfilePicture;
import dev.foundation.userservice.domain.enums.ImageTypesEnum;
import dev.foundation.userservice.errors.ValueObjectException;

public class ProfilePictureType {
    private final ImageTypesEnum profilePictureType;

    private ProfilePictureType(ImageTypesEnum imageTypesEnum){
        this.profilePictureType = imageTypesEnum;
    }

    public static ProfilePictureType create(String imageType) throws ValueObjectException {
        try {
            ImageTypesEnum type = ImageTypesEnum.valueOf(imageType.toUpperCase());
            return new ProfilePictureType(type);
        }catch (Exception e){
            if(e instanceof NullPointerException || e instanceof IllegalArgumentException){
                throw new ValueObjectException("This image type is not accepted. Please, try a new one.", ProfilePictureType.class.getSimpleName(), "Invalid Argument");
            }
        }
        return null;
    }

    public String value(){
        return this.profilePictureType.toString().toLowerCase();
    }

    @Override
    public boolean equals(Object o){
        if(this == o)return true;
        if(!(o instanceof ProfilePictureType pictureType)) return false;
        return this.profilePictureType.equals(pictureType.profilePictureType);
    }

    @Override
    public int hashCode(){
        return this.profilePictureType.hashCode();
    }
}
