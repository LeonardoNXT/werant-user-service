package dev.foundation.userservice.errors;

public class SystemException extends Exception{
    public String module;
    public String locale;
    public String violation;
    public int code;
    public SystemException(String message, String module, String locale, String violation, int code){
        super(message);
        this.module = module;
        this.locale = locale;
        this.violation = violation;
        this.code = code;
    }
}
