package de.liquiddev.command.ratelimit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TriggerBasedLimiterTest {

	@Test
	public void testNotRateLimited() {
		Object obj = new Object();
		RateLimiter ratelimit = new TriggerBasedRateLimiter(0);
		assertFalse(ratelimit.isRateLimited(obj));
		assertTrue(ratelimit.acquire(obj));
		assertTrue(ratelimit.acquire(obj));
	}

	@Test
	public void testRateLimited() {
		Object obj = new Object();
		RateLimiter ratelimit = new TriggerBasedRateLimiter(100);
		assertTrue(ratelimit.acquire(obj));
		assertFalse(ratelimit.acquire(obj));
	}

	@Test
	public void testRateLimitedMultipleTrigger() {
		Object obj1 = new Object();
		Object obj2 = new Object();
		RateLimiter ratelimit = new TriggerBasedRateLimiter(100);
		assertTrue(ratelimit.acquire(obj1));
		assertTrue(ratelimit.isRateLimited(obj1));
		assertFalse(ratelimit.acquire(obj1));
		assertTrue(ratelimit.acquire(obj2));
		assertTrue(ratelimit.isRateLimited(obj2));
		assertFalse(ratelimit.acquire(obj2));
	}
}
