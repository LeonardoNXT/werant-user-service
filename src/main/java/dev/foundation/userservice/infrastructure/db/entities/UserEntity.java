package dev.foundation.userservice.infrastructure.db.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    private UUID id;

    @Column(name="name", nullable = false, length = 100)
    private String name;

    @Column(name="username", nullable = false, length = 50)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

	@Column(name = "version", nullable = false)
	private Integer version;

	@Column(name = "phone_number", unique = true, length = 150)
	private String phoneNumber;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;

    @Column(name = "number_verified", nullable = false)
    private boolean numberVerified;

    @Column(name = "profile_picture_id", unique = true)
    private UUID profilePictureId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
