package de.liquiddev.command.autocomplete;

import de.liquiddev.command.AbstractCommandSender;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class PlayerAutocompleter<T extends CommandSender> implements Autocompleter<T> {

	private final boolean hideInvisiblePlayers;

	@Override
	public Collection<String> autocomplete(AbstractCommandSender<? extends T> sender, String startsWith) {
		String lowercase = startsWith.toLowerCase();
		return Bukkit.getOnlinePlayers()
				.stream()
				.filter(other -> !(sender.getSender() instanceof Player player) || player.canSee(other) || !hideInvisiblePlayers)
				.map(Player::getName)
				.filter(n -> n.toLowerCase().startsWith(lowercase))
				.collect(Collectors.toList());
	}
}