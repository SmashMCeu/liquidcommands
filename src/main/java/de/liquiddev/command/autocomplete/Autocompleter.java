package de.liquiddev.command.autocomplete;

import java.util.Collection;

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
	public Collection<String> autocomplete(T sender, String str);
}