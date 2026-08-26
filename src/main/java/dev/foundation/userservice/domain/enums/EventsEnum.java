package dev.foundation.userservice.domain.enums;

public class EventsEnum {
    public static enum USER_EVENTS_ENUM {
            CREATE,
            CHANGE_USERNAME,
            CHANGE_PASSWORD,
            CHANGE_EMAIL,
	        CHANGE_PHONE_NUMBER,
            CHANGE_ROLE,
            BAN,
            DEACTIVATE,
            SUSPEND,
            ACTIVATE,
    }
}
