package dev.foundation.userservice.domain.structure;

import dev.foundation.userservice.domain.value_objects.Date;
import dev.foundation.userservice.domain.value_objects.Email;

public class EmailEntryContent {
    private final Email email;
    private final Date occurredAt;

    private EmailEntryContent(Email email, Date occurredAt){
        this.email = email;
        this.occurredAt = occurredAt;
    }

    public static EmailEntryContent create(Email email, Date occurredAt){
        return new EmailEntryContent(email, occurredAt);
    }

    public Email email(){
        return this.email;
    }

    public Date occurredAt(){
        return this.occurredAt;
    }
}
