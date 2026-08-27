package dev.foundation.userservice.presentation.config.infrastructure.packages;

import dev.foundation.userservice.application.ports.IdGeneratorPort;
import dev.foundation.userservice.infrastructure.packages.IdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdGeneratorConfig {
	@Bean
	public IdGeneratorPort idGeneratorPort(){
		return new IdGenerator();
	}
}
