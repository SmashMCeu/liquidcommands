package de.liquiddev.command.adapter.bukkit;

import org.bukkit.command.CommandSender;

import de.liquiddev.command.AbstractCommandSender;

public class BukkitCommandSender<T extends CommandSender> extends AbstractCommandSender<T> {
	public BukkitCommandSender(T sender) {
		super(sender);
	}

	@Override
	public void sendMessage(String message) {
		sender.sendMessage(message);
	}

	@Override
	public boolean hasPermission(String permission) {
		return sender.hasPermission(permission);
	}

	@Override
	public String getName() {
		return sender.getName();
	}
}