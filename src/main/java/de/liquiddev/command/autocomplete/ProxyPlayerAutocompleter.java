package de.liquiddev.command.autocomplete;

import de.liquiddev.command.AbstractCommandSender;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

import java.util.Collection;
import java.util.stream.Collectors;

class ProxyPlayerAutocompleter<T extends CommandSender> implements Autocompleter<T> {

	@Override
	public Collection<String> autocomplete(AbstractCommandSender<? extends T> sender, String startsWith) {
		String lowercase = startsWith.toLowerCase();
		return ProxyServer.getInstance()
				.getPlayers()
				.stream()
				.map(CommandSender::getName)
				.filter(n -> n.toLowerCase().startsWith(lowercase))
				.collect(Collectors.toList());
	}
}