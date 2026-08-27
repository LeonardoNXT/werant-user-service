package dev.foundation.userservice.domain.pattern;

import java.util.regex.Pattern;

public class PhoneNumberPattern {
	public static Pattern PATTERN = Pattern.compile("^(\\([1-9]{2}\\)|[1-9]{2}) ?9[0-9]{4}-?[0-9]{4}$");
}
