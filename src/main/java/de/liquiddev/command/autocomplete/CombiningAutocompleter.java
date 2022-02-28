package de.liquiddev.command.autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Combine multiple AutoCompleters into one.
 * 
 * @author LiquidDev
 *
 * @param <T> type of sender who requested the autocompletion
 */
@Getter
@RequiredArgsConstructor
public class CombiningAutocompleter<T> implements Autocompleter<T> {

	private Collection<Autocompleter<? super T>> autocompleters = new ArrayList<>(2);

	// mutates
	public void addAutocompleter(Autocompleter<? super T> autocompleter) {
		this.autocompleters.add(autocompleter);
	}

	// not mutating
	@Override
	public Autocompleter<T> and(Autocompleter<? super T> other) {
		CombiningAutocompleter<T> newCompleter = new CombiningAutocompleter<>();
		newCompleter.getAutocompleters()
				.addAll(autocompleters);
		newCompleter.getAutocompleters()
				.add(other);
		return newCompleter;
	}

	@Override
	public Collection<String> autocomplete(T sender, String str) {
		List<String> completions = new ArrayList<>();
		autocompleters.forEach(all -> completions.addAll(all.autocomplete(sender, str)));
		return completions;
	}
}
