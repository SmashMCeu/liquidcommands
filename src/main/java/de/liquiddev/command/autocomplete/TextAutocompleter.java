package de.liquiddev.command.autocomplete;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;

import de.liquiddev.command.AbstractCommandSender;

class TextAutocompleter<T> implements Autocompleter<T> {

	private String[] suggestions;

	public TextAutocompleter(List<String> suggestions) {
		Preconditions.checkNotNull(suggestions, "suggestions must not be null");
		this.setSuggestions(suggestions.toArray(new String[] {}));
	}

	public TextAutocompleter(String... suggestions) {
		Preconditions.checkNotNull(suggestions, "suggestions must not be null");
		this.setSuggestions(suggestions);
	}

	private void setSuggestions(String[] str) {
		this.suggestions = Arrays.copyOf(str, str.length);
	}

	@Override
	public Collection<String> autocomplete(@SuppressWarnings("rawtypes") AbstractCommandSender sender, String startsWith) {
		String lowercase = startsWith.toLowerCase();
		return Arrays.stream(suggestions)
				.filter(str -> str.toLowerCase()
						.startsWith(lowercase))
				.collect(Collectors.toList());
	}
}