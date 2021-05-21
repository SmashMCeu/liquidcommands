package de.liquiddev.command.autocomplete;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

class TextAutocompleter<T> implements Autocompleter<T> {

	private String[] suggestions;

	public TextAutocompleter(List<String> suggestions) {
		this.setSuggestions(suggestions.toArray(new String[] {}));
	}

	public TextAutocompleter(String... suggestions) {
		this.setSuggestions(suggestions);
	}

	private void setSuggestions(String[] str) {
		this.suggestions = new String[str.length];
		for (int i = 0; i < str.length; i++) {
			this.suggestions[i] = str[i].toLowerCase();
		}
	}

	@Override
	public Collection<String> autocomplete(Object sender, String startsWith) {
		String lowercase = startsWith.toLowerCase();
		return Arrays.stream(suggestions).filter(str -> str.startsWith(lowercase)).collect(Collectors.toList());
	}
}