package dev.foundation.userservice.domain.pattern;

import java.util.regex.Pattern;

public class DatePattern {
    public static Pattern PATTERN = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z$");
}
