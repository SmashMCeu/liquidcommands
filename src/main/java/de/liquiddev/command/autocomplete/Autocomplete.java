package de.liquiddev.command.autocomplete;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class Autocomplete {

	@SuppressWarnings("rawtypes")
	public static final Autocompleter NONE = (t, s) -> Collections.emptyList();

	protected Autocomplete() {
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

	public static <T> Autocompleter<T> combine(Autocompleter<? super T>... completors) {
		CombiningAutocompleter<T> combined = new CombiningAutocompleter<>();
		for (Autocompleter<? super T> all : completors) {
			combined.addAutocompleter(all);
		}
		return combined;
	}

	@SuppressWarnings("rawtypes")
	public static <T> Autocompleter<T> players() {
		return players(true);
	}

	public static <T> Autocompleter<T> players(boolean hideInvisiblePlayers) {
		return new PlayerAutocompleter(hideInvisiblePlayers);
	}

	@SuppressWarnings("rawtypes")
	public static <T> Autocompleter<T> proxyPlayers() {
		return new ProxyPlayerAutocompleter();
	}

	public static <T> Autocompleter<T> none() {
		return NONE;
	}
}