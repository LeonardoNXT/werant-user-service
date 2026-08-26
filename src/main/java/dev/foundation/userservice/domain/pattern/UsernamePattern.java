package dev.foundation.userservice.domain.pattern;

import java.util.regex.Pattern;

public class UsernamePattern {
	public static Pattern PATTERN = Pattern.compile("^(?!.*[ ]{2,})[a-zA-Z ]+$");
}

