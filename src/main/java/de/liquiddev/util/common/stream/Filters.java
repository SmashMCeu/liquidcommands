package de.liquiddev.util.common.stream;

import java.util.function.Predicate;

public class Filters {

	public static Predicate<String> startsWith(String with) {
		return str -> str.startsWith(with);
	}

	public static Predicate<String> startsWithIgnoreCase(String with) {
		String lowercase = with.toLowerCase();
		return str -> str.toLowerCase().startsWith(lowercase);
	}

	public static Predicate<String> endsWith(String with) {
		return str -> str.endsWith(with);
	}

	public static Predicate<String> endsWithIgnoreCase(String with) {
		String lowercase = with.toLowerCase();
		return str -> str.toLowerCase().endsWith(lowercase);
	}

	public static Predicate<String> contains(String with) {
		return str -> str.contains(with);
	}

	public static Predicate<String> containsIgnoreCase(String with) {
		String lowercase = with.toLowerCase();
		return str -> str.toLowerCase().contains(lowercase);
	}

	public static <T> Predicate<T> isInstance(Class<?> type) {
		return o -> type.isInstance(o);
	}

	public static <T> Predicate<T> notNull() {
		return o -> o != null;
	}
}