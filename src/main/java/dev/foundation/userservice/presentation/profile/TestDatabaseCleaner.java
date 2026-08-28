package dev.foundation.userservice.presentation.profile;

import jakarta.persistence.EntityManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("test")
public class TestDatabaseCleaner implements CommandLineRunner {

	private final EntityManager entityManager;

	public TestDatabaseCleaner(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	@Transactional
	public void run(String... args) {
		entityManager.createNativeQuery(
				"TRUNCATE TABLE outbox, users CASCADE"
		).executeUpdate();
	}
}