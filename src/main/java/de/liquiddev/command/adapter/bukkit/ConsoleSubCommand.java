package de.liquiddev.command.adapter.bukkit;

import org.bukkit.command.CommandSender;

public abstract class ConsoleSubCommand extends SubCommand<CommandSender> {
	public ConsoleSubCommand(String name, String hint) {
		super(CommandSender.class, name, hint);
	}
}