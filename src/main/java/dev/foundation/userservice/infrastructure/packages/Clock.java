package dev.foundation.userservice.infrastructure.packages;

import dev.foundation.userservice.application.ports.ClockPort;
import dev.foundation.userservice.domain.value_objects.Date;
import dev.foundation.userservice.errors.ValueObjectException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Clock implements ClockPort {
	@Override
	public Date nowIso8601() throws ValueObjectException {
		try{
			String instant = Instant.now().truncatedTo(ChronoUnit.SECONDS).toString();
			System.out.print(instant);
			return Date.create(instant);
		} catch (ValueObjectException e){
			throw e.at(Clock.class.getSimpleName());
		}
	}
}
