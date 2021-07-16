package de.liquiddev.command.ratelimit;

import java.util.concurrent.TimeUnit;

public class RateLimit {

	private static final RateLimiter NONE = new RateLimiter() {
		@Override
		public boolean isRateLimited(Object trigger) {
			return false;
		}

		@Override
		public boolean acquire(Object trigger) {
			return true;
		}
	};

	public static final RateLimiter none() {
		return NONE;
	}

	public static RateLimiter global(long timeout, TimeUnit unit) {
		return new GlobalRateLimiter(TimeUnit.MILLISECONDS.convert(timeout, unit));
	}

	public static RateLimiter perPlayer(long timeout, TimeUnit unit) {
		return new TriggerBasedRateLimiter(TimeUnit.MILLISECONDS.convert(timeout, unit));
	}
}
