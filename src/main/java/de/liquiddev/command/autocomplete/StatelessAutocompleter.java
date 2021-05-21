package de.liquiddev.command.autocomplete;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import de.liquiddev.util.common.stream.Filters;

/**
 * A stateless autocompleter for collections. Does support filters to hide
 * unwanted elements.
 *
 * @param <T> type of elements to be auto completed
 * @param <R> type of sender
 */
@SuppressWarnings("rawtypes")
public class StatelessAutocompleter<T> implements Autocompleter {
	private Supplier<Collection<T>> supplier;
	private Function<T, String> mapper;

	@Nullable
	private Predicate<T> filters;

	public StatelessAutocompleter(Supplier<Collection<T>> supplier) {
		this(supplier, t -> t.toString());
	}

	public StatelessAutocompleter(Supplier<Collection<T>> supplier, Function<T, String> mapper) {
		this.supplier = supplier;
		this.mapper = mapper;
	}

	public StatelessAutocompleter(Supplier<Collection<T>> supplier, Function<T, String> mapper, Predicate<T>... filters) {
		this(supplier, mapper, Arrays.asList(filters));
	}

	public StatelessAutocompleter(Supplier<Collection<T>> supplier, Function<T, String> mapper, Collection<Predicate<T>> filters) {
		this.supplier = supplier;
		this.mapper = mapper;
		/* Reduce multiple predicates to one */
		this.filters = filters.stream().reduce(x -> true, Predicate::and);
	}

	@Override
	public Collection<String> autocomplete(Object sender, String startsWith) {
		String lowercase = startsWith.toLowerCase();
		Collection<T> completableObjects = this.supplier.get();
		Stream<T> completableStream = completableObjects.stream();
		if (filters != null) {
			completableStream = completableStream.filter(filters);
		}
		Stream<String> completableStringsStream = completableStream.map(mapper);
		Stream<String> suggestionsStream = completableStringsStream.filter(Filters.startsWith(lowercase));
		Collection<String> suggestions = suggestionsStream.collect(Collectors.toList());
		return suggestions;
	}
}