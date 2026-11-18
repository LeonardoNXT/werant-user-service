package dev.foundation.userservice.presentation.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RequestRegisterUserDto(
		@NotBlank(message = "Name is required.")
		@Size(max = 100, message = "Name must contain at most 100 characters.")
		@Pattern(regexp = "^((?! {2,})[a-zA-ZÀ-ÿ ])+$", message = "Name has an invalid format.")
		String name,

		@NotBlank(message = "Username is required.")
		@Size(max = 50, message = "Username must contain at most 50 characters.")
		@Pattern(regexp = "^(?!.*[ ]{2,})[a-zA-Z ]+$", message = "Username has an invalid format.")
		String username,

		@NotBlank(message = "Email is required.")
		@Email(message = "Email has an invalid format.")
		@Size(max = 150, message = "Email must contain at most 150 characters.")
		String email,

		@Pattern(regexp = "^(\\([1-9]{2}\\)|[1-9]{2}) ?9[0-9]{4}-?[0-9]{4}$", message = "Phone number has an invalid format.")
		String phoneNumber,

		@NotBlank(message = "Password is required.")
		@Size(min = 8, max = 128, message = "Password must contain between 8 and 128 characters.")
		@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d*&%$#@!?]{8,}$", message = "Password does not meet the complexity requirements.")
		String password
) {
}
