package de.liquiddev.command.ratelimit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.common.base.Preconditions;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class TriggerBasedRateLimiter implements RateLimiter {

	private final long timeout;
	private Map<Object, Long> lastActions = new HashMap<>();

	@Override
	public boolean acquire(Object trigger) {
		Preconditions.checkNotNull(trigger, "trigger must not be null");
		if (isRateLimited(trigger)) {
			return false;
		}
		this.cleanup();
		lastActions.put(trigger, System.currentTimeMillis());
		return true;
	}

	@Override
	public boolean isRateLimited(Object trigger) {
		Preconditions.checkNotNull(trigger, "trigger must not  be null");
		return System.currentTimeMillis() - lastActions.getOrDefault(trigger, 0L) < timeout;
	}

	private void cleanup() {
		Iterator<Map.Entry<Object, Long>> itr = lastActions.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<Object, Long> entry = itr.next();
			long action = entry.getValue();
			if (System.currentTimeMillis() - action > timeout) {
				itr.remove();
			}
		}
	}
}
