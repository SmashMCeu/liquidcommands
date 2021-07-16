package de.liquiddev.command.ratelimit;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class GlobalRateLimiter implements RateLimiter {

	private final long timeout;
	private long lastAction = 0;

	@Override
	public boolean acquire(Object trigger) {
		if (isRateLimited(trigger)) {
			return false;
		}
		lastAction = System.currentTimeMillis();
		return true;
	}

	@Override
	public boolean isRateLimited(Object trigger) {
		return System.currentTimeMillis() - lastAction < timeout;
	}
}
