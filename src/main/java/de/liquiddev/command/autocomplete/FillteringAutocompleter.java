package de.liquiddev.command.autocomplete;

import java.util.Collection;
import java.util.function.BiPredicate;

import de.liquiddev.command.AbstractCommandSender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Filters a given autocompleter on a per player basis.
 * 
 * @author LiquidDev
 *
 * @param <T> type of sender who requested the autocompletion
 */
@Getter
@RequiredArgsConstructor
public class FillteringAutocompleter<T> implements Autocompleter<T> {

	private final BiPredicate<T, String> filter;
	private Autocompleter<T> backingCompleter;

	public void addAutocompleter(Autocompleter<T> backingAutocompleter) {
		this.backingCompleter = backingAutocompleter;
	}

	@Override
	public Collection<String> autocomplete(AbstractCommandSender<? extends T> sender, String str) {
		return backingCompleter.autocomplete(sender, str)
				.stream()
				.filter(arg -> filter.test(sender.getSender(), arg))
				.toList();
	}
}
