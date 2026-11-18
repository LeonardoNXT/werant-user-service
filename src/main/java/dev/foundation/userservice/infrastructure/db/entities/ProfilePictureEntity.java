package dev.foundation.userservice.infrastructure.db.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name="profile_picture")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfilePictureEntity {
	@Id
	private UUID id;

	@Column(name = "key", nullable = false)
	private String key;
}
