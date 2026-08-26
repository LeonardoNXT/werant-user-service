package dev.foundation.userservice.domain.value_objects;

import dev.foundation.userservice.domain.enums.ProfilePictureStatusEnum;
import dev.foundation.userservice.errors.ValueObjectException;

public class ProfilePictureStatus {
    private final ProfilePictureStatusEnum status;

    private ProfilePictureStatus(ProfilePictureStatusEnum status){
        this.status = status;
    }

    public static ProfilePictureStatus create(String input) throws ValueObjectException {
        try {
            ProfilePictureStatusEnum status = ProfilePictureStatusEnum.valueOf(input.toUpperCase());
            return new ProfilePictureStatus(status);
        } catch (Exception e){
            if(e instanceof NullPointerException || e instanceof IllegalArgumentException){
                throw new ValueObjectException("This is not a valid Status. Please, try again with a valid Profile Status.", ProfilePictureStatus.class.getSimpleName(), "Invalid Argument");
            }
        }
        return null;
    }

    public String value(){
        return this.status.toString().toLowerCase();
    }


    @Override
    public boolean equals(Object o){
        if(this == o)return true;
        if(!(o instanceof ProfilePictureStatus pictureStatus)) return false;
        return this.status.equals(pictureStatus.status);
    }

    @Override
    public int hashCode(){
        return this.status.hashCode();
    }




}
