package dev.foundation.userservice.domain.entities;

import dev.foundation.userservice.domain.enums.ImageTypesEnum;
import dev.foundation.userservice.domain.value_objects.*;

import java.util.UUID;

public class ProfilePicture {
    private final UUID id;
    private final ProfilePictureKey key;
    private final ProfilePictureWidth width;
    private final ProfilePictureHeight height;
    private final ProfilePictureType type;
    private final Double ratio;
    private final Date createdAt;
    private final Date updatedAt;

    private ProfilePicture(UUID id, ProfilePictureKey key, ProfilePictureWidth width, ProfilePictureHeight height, ProfilePictureType type, Double ratio, Date createdAt, Date updatedAt){
        this.id = id;
        this.key = key;
        this.width = width;
        this.height = height;
        this.type = type;
        this.ratio = ratio;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ProfilePicture create(UUID id, ProfilePictureKey key, ProfilePictureWidth width, ProfilePictureHeight height, ProfilePictureType type, Double ratio, Date date){
        return new ProfilePicture(id,key,width,height,type,ratio,date,date);
    }

    public static ProfilePicture restore(UUID id, ProfilePictureKey key, ProfilePictureWidth width, ProfilePictureHeight height, ProfilePictureType type, Double ratio, Date createdAt, Date updatedAt){
        return new ProfilePicture(id,key,width,height,type,ratio, createdAt, updatedAt);
    }

    public UUID id(){
        return this.id;
    }

    public ProfilePictureKey key(){
        return this.key;
    }

    public ProfilePictureWidth width(){
        return this.width;
    }

    public ProfilePictureHeight height(){
        return this.height;
    }

    public ProfilePictureType type(){
        return this.type;
    }

    public Double ratio(){
        return this.ratio;
    }

    public Date createdAt(){
        return this.createdAt;
    }

    public Date updatedAt(){
        return this.updatedAt;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof ProfilePicture profilePicture)) return false;
        return this.id.equals(profilePicture.id());
    }

    @Override
    public int hashCode(){
        return this.id.hashCode();
    }
}
