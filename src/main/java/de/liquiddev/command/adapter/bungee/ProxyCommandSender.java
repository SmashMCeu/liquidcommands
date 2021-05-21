package de.liquiddev.command.adapter.bungee;

import de.liquiddev.command.AbstractCommandSender;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

public class ProxyCommandSender<T extends CommandSender> extends AbstractCommandSender<T> {
	public ProxyCommandSender(T sender) {
		super(sender);
	}

	@Override
	public void sendMessage(String message) {
		sender.sendMessage(TextComponent.fromLegacyText(message));
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
