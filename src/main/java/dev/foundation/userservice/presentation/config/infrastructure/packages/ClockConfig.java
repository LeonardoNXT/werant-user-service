package dev.foundation.userservice.presentation.config.infrastructure.packages;

import dev.foundation.userservice.application.ports.ClockPort;
import dev.foundation.userservice.infrastructure.packages.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClockConfig {
	@Bean
	public ClockPort clockPort(){
		return new Clock();
	}
}
