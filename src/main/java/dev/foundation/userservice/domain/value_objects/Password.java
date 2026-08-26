package dev.foundation.userservice.domain.value_objects;
import dev.foundation.userservice.domain.pattern.PasswordPattern;
import dev.foundation.userservice.errors.ValueObjectException;

public final class Password {
    private final String password;

    private Password(String password) {
        this.password = password;
    }

    public static Password create(String password) throws ValueObjectException {
        if (password == null || !PasswordPattern.PATTERN.matcher(password).matches()) {
            throw new ValueObjectException("Invalid password.", Password.class.getSimpleName(), "Invalid Argument");
        }

        return new Password(password);
    }

    public String value() {
        return this.password;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Password passwordVo))return false;
        return this.password.equals(passwordVo.value());
    }

    @Override
    public int hashCode(){
        return this.password.hashCode();
    }
}
