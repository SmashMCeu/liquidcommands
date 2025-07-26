package de.liquiddev.command.autocomplete;

import de.liquiddev.command.AbstractCommandSender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Combine multiple AutoCompleters into one.
 *
 * @param <T> type of sender who requested the autocompletion
 * @author LiquidDev
 */
@Getter
@RequiredArgsConstructor
public class CombiningAutocompleter<T> implements Autocompleter<T> {

	/**
	 * If true, the first Autocompleter that returns any result will be used for completion.
	 */
	private final boolean prioritized;
	private final Collection<Autocompleter<? super T>> autocompleters = new ArrayList<>(2);

	// mutates
	public void addAutocompleter(Autocompleter<? super T> autocompleter) {
		this.autocompleters.add(autocompleter);
	}

	// not mutating
	@Override
	public <U extends T> Autocompleter<U> and(Autocompleter<U> other) {
		CombiningAutocompleter<U> newCompleter = new CombiningAutocompleter<>(false);
		newCompleter.getAutocompleters()
				.addAll(autocompleters);
		newCompleter.getAutocompleters()
				.add(other);
		return newCompleter;
	}

	@Override
	public Collection<String> autocomplete(AbstractCommandSender<? extends T> sender, String str) {
		List<String> completions = new ArrayList<>();
		for (Autocompleter<? super T> all : autocompleters) {
			completions.addAll(all.autocomplete(sender, str));
			if (prioritized && !completions.isEmpty()) {
				break; // We found something - stop looking for more
			}
		}
		return completions;
	}
}
