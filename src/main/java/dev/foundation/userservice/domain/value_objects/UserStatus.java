package dev.foundation.userservice.domain.value_objects;

import dev.foundation.userservice.domain.enums.StatusEnum;
import dev.foundation.userservice.errors.ValueObjectException;

public class UserStatus {
    private final StatusEnum status;

    private UserStatus(StatusEnum status){
        this.status = status;
    }

	public static UserStatus create(String status) throws ValueObjectException {

		if (status == null || status.isBlank()) {
			throw new ValueObjectException("User status cannot be empty.", UserStatus.class.getSimpleName(), "Invalid Argument");
		}

		String cleanStatus = status.trim().toLowerCase();

		boolean condition = cleanStatus.equals("active") || cleanStatus.equals("deactivated") ||
				cleanStatus.equals("banned") || cleanStatus.equals("suspended");

		if(!condition){

			throw new ValueObjectException("Invalid status provided. Must be active, deactivated, banned, or suspended.", UserStatus.class.getSimpleName(), "Invalid Argument");
		}


		StatusEnum statusEnum = StatusEnum.valueOf(cleanStatus.toUpperCase());
		return new UserStatus(statusEnum);
	}


    public String value(){
        return this.status.toString().toLowerCase();
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof UserStatus userStatusVo))return false;
        return this.status.toString().toLowerCase().equals(userStatusVo.value());
    }

    @Override
    public int hashCode(){
        return this.status.hashCode();
    }

}
