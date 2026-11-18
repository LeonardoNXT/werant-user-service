package dev.foundation.userservice.presentation.config.infrastructure.packages;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
		prefix = "foundation.storage.minio",
		name = "enabled",
		havingValue = "true"
)
public class MinIOConfig {
	// The client bean will be added with the profile-picture storage adapter.
}
