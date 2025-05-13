package de.liquiddev.command.adapter.bukkit;

import de.liquiddev.command.AbstractCommandSender;
import de.liquiddev.command.CommandArguments;
import de.liquiddev.command.CommandFailException;
import de.liquiddev.command.CommandRoot;
import de.liquiddev.command.autocomplete.Autocomplete;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

abstract class BukkitCommand<T extends CommandSender> extends CommandRoot<T> {

	private BukkitCommandAdapter adapter;

	public BukkitCommand(Class<T> type, String name) {
		this(type, name, "");
	}

	public BukkitCommand(Class<T> type, String name, String hint) {
		super(type, name, hint);
		this.adapter = new BukkitCommandAdapter(this);
		this.setDefaultAutocompleter(Autocomplete.none());
	}

	public void register(Plugin plugin) {
		adapter.register(plugin);
	}

	public void tryUnregister() {
		this.adapter.tryUnregister();
	}

	@Override
	protected void onCommand(AbstractCommandSender<T> sender, CommandArguments args) throws CommandFailException {
		this.onCommand(sender.getSender(), (Arguments) args);
	}

	protected abstract void onCommand(T sender, Arguments args) throws CommandFailException;
}