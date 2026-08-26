package dev.foundation.userservice.domain.value_objects;

import dev.foundation.userservice.domain.enums.RoleEnum;
import dev.foundation.userservice.errors.ValueObjectException;

public class Role {
	private final RoleEnum role;

	private Role(RoleEnum role) {
		this.role = role;
	}

	public static Role create(String role) throws ValueObjectException {
		if (role == null || role.isBlank()) {
			throw new ValueObjectException("Role cannot be empty.", Role.class.getSimpleName(), "Invalid Argument");
		}

		String cleanRole = role.trim().toLowerCase();

		boolean condition = cleanRole.equals("user") || cleanRole.equals("admin") ||
				cleanRole.equals("developer") || cleanRole.equals("owner");

		if (!condition) {
			throw new ValueObjectException("Invalid role provided. Must be user, admin, developer, or owner.", Role.class.getSimpleName(), "Invalid Argument");
		}

		RoleEnum roleEnum = RoleEnum.valueOf(cleanRole.toUpperCase());
		return new Role(roleEnum);
	}

	public boolean equals(String role) {
		if (role == null || role.isBlank()) return false;
		return this.role.name().equalsIgnoreCase(role.trim());
	}

	public String value() {
		return this.role.toString().toLowerCase();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Role roleVo)) return false;
		return this.role.toString().toLowerCase().equals(roleVo.value());
	}

	@Override
	public int hashCode() {
		return this.role.hashCode();
	}
}