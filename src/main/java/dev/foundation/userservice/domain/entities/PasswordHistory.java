package dev.foundation.userservice.domain.entities;

import dev.foundation.userservice.domain.structure.EmailEntryContent;
import dev.foundation.userservice.domain.structure.PasswordEntryContent;
import dev.foundation.userservice.domain.value_objects.Date;

import java.util.List;
import java.util.UUID;

public class PasswordHistory {
    private final UUID id;
    private final UUID userId;
    private final List<PasswordEntryContent> data;
    private final Date createdAt;
    private final Date updatedAt;

    private PasswordHistory(UUID id, UUID userId, List<PasswordEntryContent> data, Date createdAt, Date updatedAt){
        this.id = id;
        this.userId = userId;
        this.data = data;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PasswordHistory create(UUID id, UUID userId, List<PasswordEntryContent> data, Date date){
        return new PasswordHistory(id,userId,data, date, date);
    }

    public static PasswordHistory restore(UUID id, UUID userId, List<PasswordEntryContent> data, Date createdAt, Date updatedAt){
        return new PasswordHistory(id,userId,data, createdAt, updatedAt);
    }

    public UUID id(){
        return this.id;
    }

    public UUID userId(){
        return this.userId;
    }

    public List<PasswordEntryContent> data(){
        return this.data;
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
        if(!(o instanceof PasswordHistory passwordHistory)) return false;
        return this.id.equals(passwordHistory.id());
    }

    @Override
    public int hashCode(){
        return this.id.hashCode();
    }
}
