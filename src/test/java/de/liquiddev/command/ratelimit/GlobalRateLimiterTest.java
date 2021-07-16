package de.liquiddev.command.ratelimit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class GlobalRateLimiterTest {

	@Test
	public void testNotRateLimited() {
		RateLimiter ratelimit = new GlobalRateLimiter(0);
		assertFalse(ratelimit.isRateLimited(null));
		assertTrue(ratelimit.acquire(null));
		assertTrue(ratelimit.acquire(null));
	}

	@Test
	public void testRateLimited() {
		RateLimiter ratelimit = new GlobalRateLimiter(100);
		assertTrue(ratelimit.acquire(null));
		assertTrue(ratelimit.isRateLimited(null));
		assertFalse(ratelimit.acquire(null));
	}

}
