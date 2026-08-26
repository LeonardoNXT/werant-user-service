package dev.foundation.userservice.infrastructure.db.mappers;

import dev.foundation.userservice.domain.entities.User;
import dev.foundation.userservice.domain.value_objects.*;
import dev.foundation.userservice.errors.ValueObjectException;
import dev.foundation.userservice.infrastructure.db.entities.UserEntity;

public class UserMapper {
   public static UserEntity toOrm(User user){
        return UserEntity.builder().id(user.id()).name(user.name().value()).username(user.username().value()).email(user.email().value()).password(user.password().value()).status(user.status().value()).role(user.role().value()).emailVerified(user.emailVerified()).numberVerified(user.numberVerified()).profilePictureId(user.profilePictureId()).createdAt(user.createdAt().value()).updatedAt(user.updatedAt().value()).build();
   }

   public static User toDomain(UserEntity entity) throws ValueObjectException {
	   try{
		   Name name = Name.create(entity.getName());
		   Username username = Username.create(entity.getUsername());
		   Email email = Email.create(entity.getEmail());
		   HashedPassword password = HashedPassword.create(entity.getPassword());
		   UserStatus status = UserStatus.create(entity.getStatus());
		   Role role = Role.create(entity.getRole());
		   Date createdAt = Date.create(entity.getCreatedAt().toString());
		   Date updatedAt = Date.create(entity.getUpdatedAt().toString());

		   return  User.restore(entity.getId(), name, username, status, role, email, password, entity.getProfilePictureId(), entity.isEmailVerified(), entity.isNumberVerified(), createdAt, updatedAt);
	   }
	   catch (ValueObjectException e){
		   throw e.at(UserMapper.class.getSimpleName());
	   }
   }
}
