package dev.foundation.userservice.presentation.config.infrastructure.packages;

import dev.foundation.userservice.application.ports.PasswordHasherPort;
import dev.foundation.userservice.infrastructure.packages.PasswordHasher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PasswordHasherConfig {
	@Bean
	public PasswordHasherPort passwordHasherPort(){
		return new PasswordHasher();
	}
}
