package de.liquiddev.command.ratelimit;

public interface RateLimiter {

	/**
	 * Returns <code>true</code> if the request should not be rate limited. Returns
	 * <code>false</code> if acquiring failed and the request is rate limited.
	 * 
	 * @param trigger object to acquire on
	 * @return <code>false</code> if acquire failed and request is rate limited
	 */
	public boolean acquire(Object trigger);

	/**
	 * Checks if the rate limit should be applied for a given trigger.
	 * 
	 * @param trigger object to acquire on
	 * @return <code>true</code> is request is rate limited
	 */
	public boolean isRateLimited(Object trigger);

	/**
	 * Resets / clears the rate limit for the given trigger.
	 * 
	 * @param trigger to be reset
	 */
	void reset(Object trigger);

}
