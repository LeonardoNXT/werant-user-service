package dev.foundation.userservice.domain.entities;

import dev.foundation.userservice.domain.aggregate.AggregateRoot;
import dev.foundation.userservice.domain.structure.EmailEntryContent;
import dev.foundation.userservice.domain.value_objects.Date;
import dev.foundation.userservice.domain.value_objects.Email;

import java.util.List;
import java.util.UUID;

public class EmailHistory extends AggregateRoot {
    private final UUID id;
    private final UUID userId;
    private final List<EmailEntryContent> data;
    private final Date createdAt;
    private final Date updatedAt;

    private EmailHistory(UUID id, UUID userId, List<EmailEntryContent> data, Date createdAt, Date updatedAt){
        super();
        this.id = id;
        this.userId = userId;
        this.data = data;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static EmailHistory create(UUID id, UUID userId, List<EmailEntryContent> data, Date date){
        return new EmailHistory(id, userId, data, date, date);
    }

    public static EmailHistory restore(UUID id, UUID userId, List<EmailEntryContent> data, Date createdAt, Date updatedAt){
        return new EmailHistory(id,userId, data, createdAt, updatedAt);
    }

    public void addEmailToHistory(Email email, Date occurredAt){
        EmailEntryContent emailContent = EmailEntryContent.create(email, occurredAt);
        this.data.add(emailContent);
    }

    public UUID id(){
        return this.id;
    }

    public UUID userId(){
        return this.userId;
    }

    public List<EmailEntryContent> data(){
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
        if(!(o instanceof EmailHistory emailHistory)) return false;
        return this.id.equals(emailHistory.id);
    }

    @Override
    public int hashCode(){
        return this.id.hashCode();
    }
}
