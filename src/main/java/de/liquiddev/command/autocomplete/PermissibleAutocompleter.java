package de.liquiddev.command.autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.BiFunction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Make an AutoCompleter to only work for players with given permissions.
 * 
 * @author LiquidDev
 *
 * @param <T> type of sender who requested the autocompletion
 */
@Getter
@RequiredArgsConstructor
public class PermissibleAutocompleter<T> implements Autocompleter<T> {

	private final Autocompleter<? super T> backingAutocompleter;
	private final BiFunction<? super T, String, Boolean> permissionChecker;

	private Collection<String> permissions = new HashSet<>(1);

	/**
	 * Attaches a permission to the autocompleter.
	 * 
	 * @param permission permission to use this autocompleter
	 * @return this
	 */
	public PermissibleAutocompleter<T> withPermission(String permission) {
		this.permissions.add(permission);
		return this;
	}

	@Override
	public Collection<String> autocomplete(T sender, String str) {
		for (String permission : permissions) {
			if (!permissionChecker.apply(sender, permission)) {
				return new ArrayList<>(0);
			}
		}
		return backingAutocompleter.autocomplete(sender, str);
	}
}
