package dev.foundation.userservice.application.usecases.user.findByEmail;

import dev.foundation.userservice.application.ports.ImagePresignUrlGeneratorPort;
import dev.foundation.userservice.application.ports.UserQueryRepositoryPort;
import dev.foundation.userservice.application.usecases.Usecase;
import dev.foundation.userservice.application.usecases.user.findByEmail.helpers.UserAndProfilePictureKey;
import dev.foundation.userservice.domain.entities.User;
import dev.foundation.userservice.domain.value_objects.Email;
import dev.foundation.userservice.domain.value_objects.Link;
import dev.foundation.userservice.errors.SystemException;

public class FindByEmailUsecase implements Usecase<FindByEmailInput, FindByEmailOutput> {
	private final UserQueryRepositoryPort userQueryRepositoryPort;
	private final ImagePresignUrlGeneratorPort imagePresignUrlGeneratorPort;

	private FindByEmailUsecase(UserQueryRepositoryPort userQueryRepositoryPort, ImagePresignUrlGeneratorPort imagePresignUrlGeneratorPort){
		this.userQueryRepositoryPort = userQueryRepositoryPort;
		this.imagePresignUrlGeneratorPort = imagePresignUrlGeneratorPort;
	}

	public static FindByEmailUsecase create(UserQueryRepositoryPort userQueryRepositoryPort, ImagePresignUrlGeneratorPort imagePresignUrlGeneratorPort){
		return new FindByEmailUsecase(userQueryRepositoryPort, imagePresignUrlGeneratorPort);
	}

	@Override
	public FindByEmailOutput execute(FindByEmailInput input) throws SystemException {
		Email email = Email.create(input.email());
		UserAndProfilePictureKey userAndProfilePictureKey = this.userQueryRepositoryPort.findByEmail(email);

		Link url = this.imagePresignUrlGeneratorPort.generate(userAndProfilePictureKey.Key());

		return this.output(userAndProfilePictureKey.user(), url);
	}

	private FindByEmailOutput output(User user, Link url){
		return new FindByEmailOutput(user.name().value(),user.username().value(),user.email().value(), url.value().toString(), user.status().value(), user.emailVerified(), user.numberVerified(),user.createdAt().value(), user.updatedAt().value());
	}
}
