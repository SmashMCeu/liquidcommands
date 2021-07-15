package de.liquiddev.command.ratelimit;

public class RateLimit {

	private static final RateLimiter NONE = o -> true;

	public static final RateLimiter none() {
		return NONE;
	}
}
