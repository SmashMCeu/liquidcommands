package de.liquiddev.command.adapter.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

import de.liquiddev.command.AbstractCommandSender;
import de.liquiddev.command.CommandArguments;
import de.liquiddev.command.CommandFailException;
import de.liquiddev.command.CommandRoot;
import de.liquiddev.command.autocomplete.Autocomplete;

abstract class BukkitCommand<T extends CommandSender> extends CommandRoot<T> {

	private BukkitCommandAdapter adapter;

	public BukkitCommand(Class<T> type, String prefix, String name, String hint) {
		super(type, name, hint, prefix);
		this.adapter = new BukkitCommandAdapter(this);
		this.setDefaultAutocompleter(Autocomplete.completePlayers());
	}

	public void register(Plugin plugin) {
		PluginCommand command = Bukkit.getPluginCommand(this.getName());
		command.setExecutor(adapter);
		command.setTabCompleter(adapter);
	}

	@Override
	protected void onCommand(AbstractCommandSender<T> sender, CommandArguments args) throws CommandFailException {
		this.onCommand(sender.getSender(), (Arguments) args);
	}

	protected abstract void onCommand(T sender, Arguments args) throws CommandFailException;
}