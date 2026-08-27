package dev.foundation.userservice.presentation.dtos;

public record RequestRegisterUserDto(
		String name,
		String username,
		String email,
		String phoneNumber,
		String password
) {
}
