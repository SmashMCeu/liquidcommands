package de.liquiddev.command.adapter.bukkit.quick;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.liquiddev.command.CommandFailException;
import de.liquiddev.command.adapter.bukkit.Arguments;
import de.liquiddev.command.adapter.bukkit.PlayerCommand;
import de.liquiddev.command.autocomplete.Autocompleter;
import de.liquiddev.command.ratelimit.RateLimit;
import de.liquiddev.command.ratelimit.RateLimiter;

public class QuickCommand {

	public static QuickCommand name(String name) {
		return new QuickCommand(name);
	}

	private final String name;
	private String prefix = "";
	private String hint = "";
	private String permission = null;
	private String[] alias = {};
	private RateLimiter rateLimiter = RateLimit.none();
	private Map<Integer, Autocompleter<Player>> autocompleters = new HashMap<>(0);

	private QuickCommand(String name) {
		this.name = name;
	}

	public QuickCommand prefix(String prefix) {
		this.prefix = prefix;
		return this;
	}

	public QuickCommand alias(String... aliases) {
		this.alias = aliases;
		return this;
	}

	public QuickCommand autocomplete(int index, Autocompleter<Player> autocompleter) {
		this.autocompleters.put(index, autocompleter);
		return this;
	}

	public QuickCommand ratelimit(RateLimiter ratelimit) {
		this.rateLimiter = ratelimit;
		return this;
	}

	public PlayerCommand register(Plugin plugin, QuickCommandExecutor executor) {
		PlayerCommand command = new PlayerCommand(prefix, name, hint) {
			@Override
			protected void onCommand(Player sender, Arguments args) throws CommandFailException {
				executor.execute(sender, args);
			}
		};
		for (Map.Entry<Integer, Autocompleter<Player>> entry : autocompleters.entrySet()) {
			command.setAutocompleter(entry.getKey(), entry.getValue());
		}
		command.setPermission(permission);
		command.setAliases(alias);
		command.setRatelimit(rateLimiter);
		command.register(plugin);
		return command;
	}

}
