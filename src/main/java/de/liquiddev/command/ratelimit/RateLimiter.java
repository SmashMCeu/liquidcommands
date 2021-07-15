package de.liquiddev.command.ratelimit;

public interface RateLimiter {
	
	public boolean tryAcquire(Object on);

}
