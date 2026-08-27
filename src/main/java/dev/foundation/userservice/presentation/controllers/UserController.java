package dev.foundation.userservice.presentation.controllers;

import dev.foundation.userservice.application.usecases.register.RegisterInputDto;
import dev.foundation.userservice.application.usecases.register.RegisterOutputDto;
import dev.foundation.userservice.application.usecases.register.RegisterUsecase;
import dev.foundation.userservice.errors.SystemException;
import dev.foundation.userservice.presentation.dtos.RequestRegisterUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController()
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	private final RegisterUsecase registerUsecase;

	@PostMapping()
	public ResponseEntity<RegisterOutputDto> createUser(@RequestBody RequestRegisterUserDto requestRegisterUserDto) throws SystemException {
		RegisterInputDto input = new RegisterInputDto(requestRegisterUserDto.name(),requestRegisterUserDto.username(),requestRegisterUserDto.email(), Optional.ofNullable(requestRegisterUserDto.phoneNumber()), requestRegisterUserDto.password(), false,false);
		RegisterOutputDto registerOutputDto = this.registerUsecase.execute(input);
		return ResponseEntity.status(HttpStatus.CREATED).body(registerOutputDto);
	}
}
