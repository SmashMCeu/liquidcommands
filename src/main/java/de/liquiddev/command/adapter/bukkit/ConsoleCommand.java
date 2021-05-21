package de.liquiddev.command.adapter.bukkit;

import org.bukkit.command.CommandSender;

public abstract class ConsoleCommand extends BukkitCommand<CommandSender> {

	public ConsoleCommand(String prefix, String name, String hint) {
		super(CommandSender.class, prefix, name, hint);
	}
}