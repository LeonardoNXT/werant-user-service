package dev.foundation.userservice.domain.value_objects;

import dev.foundation.userservice.domain.pattern.PhoneNumberPattern;
import dev.foundation.userservice.errors.ValueObjectException;

public final class PhoneNumber {
	private final String phoneNumber;

	private PhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}

	public static PhoneNumber create(String phoneNumber) throws ValueObjectException{

		if(phoneNumber == null || phoneNumber.isBlank()){
			throw exception("Phone number cannot be null or empty.");
		}

		String trimmedPhoneNumber = phoneNumber.trim();

		if(!PhoneNumberPattern.PATTERN.matcher(trimmedPhoneNumber).matches()){
			throw exception("The input must match the required format.");
		}

		return new PhoneNumber(trimmedPhoneNumber);
	}

	public String value(){
		return this.phoneNumber;
	}

	@Override
	public boolean equals(Object o){
		if(this == o) return true;
		if(!(o instanceof PhoneNumber PN)) return false;
		return this.value().equals(PN.phoneNumber);
	}

	@Override
	public int hashCode(){
		return this.value().hashCode();
	}

	private static ValueObjectException exception(String message) {
		return new ValueObjectException(message, PhoneNumber.class.getSimpleName(), "Invalid Argument");
	}
}
