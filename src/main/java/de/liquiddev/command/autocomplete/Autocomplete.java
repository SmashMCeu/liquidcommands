package de.liquiddev.command.autocomplete;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class Autocomplete {

	@SuppressWarnings("rawtypes")
	public static final Autocompleter NONE = (t, s) -> Collections.emptyList();

	private Autocomplete() {
	}

	public static <T> Autocompleter<T> text(String... text) {
		return new TextAutocompleter<T>(text);
	}

	public static <T> Autocompleter<T> text(List<String> text) {
		return new TextAutocompleter<T>(text);
	}

	public static <T> Autocompleter<T> array(Collection<T> array) {
		return new StatelessAutocompleter<>(() -> array);
	}

	public static <T, R> Autocompleter<T> mapped(Collection<R> array, Function<R, String> mapFunction) {
		return new StatelessAutocompleter<>(() -> array, mapFunction);
	}

	public static <T, R> Autocompleter<T> supply(Supplier<Collection<R>> supplier) {
		return new StatelessAutocompleter<>(supplier);
	}

	public static <T, R> Autocompleter<T> supplyMapped(Supplier<Collection<R>> supplier, Function<R, String> mapFunction) {
		return new StatelessAutocompleter<>(supplier, mapFunction);
	}

	@SuppressWarnings("rawtypes")
	public static <T> Autocompleter<T> players() {
		return new PlayerAutocompleter();
	}

	@SuppressWarnings("rawtypes")
	public static <T> Autocompleter<T> proxyPlayers() {
		return new ProxyPlayerAutocompleter();
	}

	public static <T> Autocompleter<T> none() {
		return NONE;
	}

	/* Deprecated - just for compatibility */
	@Deprecated
	public static <T> Autocompleter<T> completeText(String... text) {
		return new TextAutocompleter<T>(text);
	}

	@Deprecated
	public static <T> Autocompleter<T> completeText(List<String> text) {
		return new TextAutocompleter<T>(text);
	}

	@Deprecated
	public static <T> Autocompleter<T> complete(Collection<T> array) {
		return new StatelessAutocompleter<>(() -> array);
	}

	@Deprecated
	public static <T, R> Autocompleter<T> complete(Collection<R> array, Function<R, String> mapFunction) {
		return new StatelessAutocompleter<>(() -> array, mapFunction);
	}

	@Deprecated
	public static <T, R> Autocompleter<T> complete(Supplier<Collection<R>> supplier) {
		return new StatelessAutocompleter<>(supplier);
	}

	@Deprecated
	public static <T, R> Autocompleter<T> complete(Supplier<Collection<R>> supplier, Function<R, String> mapFunction) {
		return new StatelessAutocompleter<>(supplier, mapFunction);
	}

	@SuppressWarnings("rawtypes")
	@Deprecated
	public static <T> Autocompleter<T> completePlayers() {
		return new PlayerAutocompleter();
	}

	@SuppressWarnings("rawtypes")
	@Deprecated
	public static <T> Autocompleter<T> completeProxyPlayers() {
		return new ProxyPlayerAutocompleter();
	}
}