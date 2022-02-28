package de.liquiddev.command.autocomplete;

import java.util.Collection;

import de.liquiddev.command.AbstractCommandSender;

/**
 * 
 * @author LiquidDev
 *
 * @param <T> type of sender (cause of request)
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
	public Collection<String> autocomplete(AbstractCommandSender<? extends T> sender, String str);

	/**
	 * Combine the AutoCompleter with another one. This will not mutate the current
	 * {@link Autocompleter} but create a new {@link CombiningAutocompleter}.
	 * 
	 * 
	 * @param other The AutoCompleter to be combined with.
	 * @return New AutoCompleter that acts like both this and the other combined.
	 */
	public default Autocompleter<T> and(Autocompleter<? super T> other) {
		CombiningAutocompleter<T> combined = new CombiningAutocompleter<>();
		combined.addAutocompleter(this);
		combined.addAutocompleter(other);
		return combined;
	}

	/**
	 * Adds a permission that the sender must have to use this AutoCompleter. This
	 * will not mutate the current {@link Autocompleter} but create a new
	 * {@link PermissibleAutocompleter}.
	 * 
	 * @param permission Permission to be checked
	 * @return New AutoCompleter with given permission
	 */
	public default Autocompleter<T> withPermission(String permission) {
		PermissibleAutocompleter<T> permissible = new PermissibleAutocompleter<>(this);
		permissible.addPermission(permission);
		return permissible;
	}
}