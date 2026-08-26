package dev.foundation.userservice.domain.pattern;

import java.util.regex.Pattern;

public class EmailPattern {
	public static final Pattern PATTERN = Pattern.compile(
			"^(?=[a-zA-Z0-9._%+-]{1,64}@)(?!\\.)(?!.*\\.@)(?!.*\\.\\.)[a-zA-Z0-9._%+-]+@" +
					"(?=[a-zA-Z0-9.-]{3,255}\\z)(?!.*\\.\\.)[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\\z"
	);
}
