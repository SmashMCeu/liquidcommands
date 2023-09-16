package de.liquiddev.command.adapter.bukkit;

import org.bukkit.command.CommandSender;

public abstract class ConsoleCommand extends BukkitCommand<CommandSender> {
	public ConsoleCommand(String name, String hint) {
		super(CommandSender.class, name, hint);
	}

	public ConsoleCommand(String name) {
		super(CommandSender.class, name);
	}
}