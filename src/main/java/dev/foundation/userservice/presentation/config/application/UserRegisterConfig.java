package dev.foundation.userservice.presentation.config.application;

import dev.foundation.userservice.application.ports.ClockPort;
import dev.foundation.userservice.application.ports.IdGeneratorPort;
import dev.foundation.userservice.application.ports.PasswordHasherPort;
import dev.foundation.userservice.application.ports.UserCommandRepositoryPort;
import dev.foundation.userservice.application.usecases.register.RegisterUsecase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserRegisterConfig {
	
	@Bean
	public RegisterUsecase registerUsecase(UserCommandRepositoryPort userCommandRepositoryPort,IdGeneratorPort idGeneratorPort,PasswordHasherPort passwordHasherPort,ClockPort clockPort){
		return RegisterUsecase.create(userCommandRepositoryPort, passwordHasherPort, idGeneratorPort, clockPort);
	}

}
