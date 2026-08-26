package dev.foundation.userservice.domain.pattern;

import java.util.regex.Pattern;

public class PasswordPattern {
    public static final Pattern PATTERN =          Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d*&%$#@!?]{8,}$"
    );
}
