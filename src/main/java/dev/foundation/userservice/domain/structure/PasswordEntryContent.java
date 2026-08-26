package dev.foundation.userservice.domain.structure;

import dev.foundation.userservice.domain.value_objects.Date;
import dev.foundation.userservice.domain.value_objects.HashedPassword;

public class PasswordEntryContent {
    private final HashedPassword password;
    private final Date occurredAt;

    private PasswordEntryContent(HashedPassword password, Date occurredAt){
        this.password = password;
        this.occurredAt = occurredAt;
    }

    public static PasswordEntryContent create(HashedPassword password, Date occurredAt){
        return new PasswordEntryContent(password,occurredAt);
    }

    public HashedPassword password(){
        return this.password;
    }

    public Date occurredAt(){
        return this.occurredAt;
    }
}
