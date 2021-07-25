package de.liquiddev.command.context;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import de.liquiddev.command.CommandRoot;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommandContext {

	@NonNull
	private final CommandRoot<?> root;
	private Map<Class<?>, Object> contextMap = new HashMap<>();

	public <T> void addContext(Class<T> type, T context) throws IllegalStateException {
		if (hasContext(type)) {
			throw new IllegalStateException("command already has context of type " + type.getName());
		}
		this.contextMap.put(type, context);
	}

	public boolean hasContext(Class<?> type) {
		return contextMap.containsKey(type);
	}

	@Nonnull
	public <T> T getContext(Class<T> type) throws IllegalArgumentException {
		Object ctx = contextMap.get(type);
		if (ctx == null) {
			throw new IllegalArgumentException("no context of type " + type.getName() + " found in command '" + root.getName() + "'");
		}
		return (T) ctx;
	}
}
