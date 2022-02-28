package de.liquiddev.command.autocomplete;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;

import com.google.common.base.Preconditions;

import de.liquiddev.command.AbstractCommandSender;
import de.liquiddev.util.common.stream.Filters;

/**
 * A stateless autocompleter for collections. Does support filters to hide
 * unwanted elements.
 *
 * @param <T> type of elements to be auto completed
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
		Preconditions.checkNotNull(supplier, "supplier must not be null");
		Preconditions.checkNotNull(mapper, "mapper must not be null");
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
		this.filters = filters.stream()
				.reduce(x -> true, Predicate::and);
	}

	@Override
	public Collection<String> autocomplete(AbstractCommandSender sender, String startsWith) {
		Collection<T> completableObjects = this.supplier.get();
		Stream<T> completableStream = completableObjects.stream();
		if (filters != null) {
			completableStream = completableStream.filter(filters);
		}
		Stream<String> completableStringsStream = completableStream.map(mapper);
		Stream<String> suggestionsStream = completableStringsStream.filter(Filters.startsWithIgnoreCase(startsWith));
		Collection<String> suggestions = suggestionsStream.collect(Collectors.toList());
		return suggestions;
	}

}