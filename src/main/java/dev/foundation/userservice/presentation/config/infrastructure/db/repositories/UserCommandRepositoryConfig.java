package dev.foundation.userservice.presentation.config.infrastructure.db.repositories;

import dev.foundation.userservice.application.ports.UserCommandRepositoryPort;
import dev.foundation.userservice.infrastructure.db.repositories.commands.UserCommandRepository;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserCommandRepositoryConfig {

	@Bean
	public UserCommandRepositoryPort userCommandRepositoryPort(
			EntityManager entityManager
	) {
		return UserCommandRepository.create(entityManager);
	}
}