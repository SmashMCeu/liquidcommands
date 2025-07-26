package de.liquiddev.command.autocomplete;

import de.liquiddev.command.AbstractCommandSender;

import java.util.Collection;
import java.util.function.BiPredicate;

/**
 * @param <T> type of sender (cause of request)
 * @author LiquidDev
 */
public interface Autocompleter<T> {
	/**
	 * Autocompletes the given {@link String} and returns the possible
	 * autocompletions as a {@link Collection} of {@link String}.
	 *
	 * @param sender trigger of the autocompletion
	 * @param str    input string to be autocompleted
	 * @return possible autocompletions
	 */
	Collection<String> autocomplete(AbstractCommandSender<? extends T> sender, String str);

	/**
	 * Combine the AutoCompleter with another one. This will not mutate the current
	 * {@link Autocompleter} but create a new {@link CombiningAutocompleter}.
	 *
	 * @param other The AutoCompleter to be combined with.
	 * @return New AutoCompleter that acts like both this and the other combined.
	 */
	default <U extends T> Autocompleter<U> and(Autocompleter<U> other) {
		CombiningAutocompleter<U> combined = new CombiningAutocompleter<>(false);
		combined.addAutocompleter(this);
		combined.addAutocompleter(other);
		return combined;
	}

	/**
	 * Combine the AutoCompleter with another one, but prioritizes the first one. This will not mutate the current
	 * {@link Autocompleter} but create a new {@link CombiningAutocompleter} with prioritization enabled.
	 *
	 * @param other The AutoCompleter to be combined with.
	 * @return New AutoCompleter that first uses this one and then falls back to the other one.
	 */
	default <U extends T> Autocompleter<U> orElse(Autocompleter<U> other) {
		CombiningAutocompleter<U> prioritized = new CombiningAutocompleter<>(true);
		prioritized.addAutocompleter(this); // prioritize this 
		prioritized.addAutocompleter(other);
		return prioritized;
	}

	/**
	 * Adds a permission that the sender must have to use this AutoCompleter. This
	 * will not mutate the current {@link Autocompleter} but create a new
	 * {@link PermissibleAutocompleter}.
	 *
	 * @param permission Permission to be checked
	 * @return New AutoCompleter with given permission
	 */
	default Autocompleter<T> withPermission(String permission) {
		PermissibleAutocompleter<T> permissible = new PermissibleAutocompleter<>(this);
		permissible.addPermission(permission);
		return permissible;
	}

	/**
	 * Adds a filter to the autocompleter that can filter specific arguments on a
	 * per-player-basis.
	 *
	 * @param filter for the Autocompleted statements
	 * @return new {@link Autocompleter} with the filter applied
	 */
	default Autocompleter<T> filter(BiPredicate<T, String> filter) {
		FillteringAutocompleter<T> filtering = new FillteringAutocompleter<T>(filter);
		filtering.addAutocompleter(this);
		return filtering;
	}
}
