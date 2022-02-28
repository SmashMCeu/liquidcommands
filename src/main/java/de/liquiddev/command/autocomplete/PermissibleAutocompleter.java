package de.liquiddev.command.autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import de.liquiddev.command.AbstractCommandSender;
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

	private final Autocompleter<? extends T> backingAutocompleter;

	private Collection<String> permissions = new HashSet<>(1);

	/**
	 * Attaches a permission to the autocompleter.
	 * 
	 * @param permission permission to use this autocompleter
	 */
	public void addPermission(String permission) {
		this.permissions.add(permission);
	}

	@Override
	public Collection<String> autocomplete(AbstractCommandSender<? super T> sender, String str) {
		for (String permission : permissions) {
			if (!sender.hasPermission(permission)) {
				return new ArrayList<>(0);
			}
		}
		return backingAutocompleter.autocomplete(sender, str);
	}
}
