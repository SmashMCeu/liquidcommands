package de.liquiddev.command.autocomplete;

import java.util.Collection;
import java.util.stream.Collectors;

import de.liquiddev.command.AbstractCommandSender;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

class ProxyPlayerAutocompleter<T extends CommandSender> implements Autocompleter<T> {

	@Override
	public Collection<String> autocomplete(AbstractCommandSender<? super T> sender, String startsWith) {
		String lowercase = startsWith.toLowerCase();
		return ProxyServer.getInstance()
				.getPlayers()
				.stream()
				.map(p -> p.getName())
				.filter(n -> n.toLowerCase()
						.startsWith(lowercase))
				.collect(Collectors.toList());
	}
}