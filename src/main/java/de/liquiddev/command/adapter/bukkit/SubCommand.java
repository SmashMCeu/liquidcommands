package de.liquiddev.command.adapter.bukkit;

import org.bukkit.command.CommandSender;

import de.liquiddev.command.AbstractCommandSender;
import de.liquiddev.command.CommandArguments;
import de.liquiddev.command.CommandChild;
import de.liquiddev.command.CommandFailException;

abstract class SubCommand<T extends CommandSender> extends CommandChild<T> {

	public SubCommand(Class<T> senderType, String name, String hint) {
		super(senderType, name, hint);
	}

	@Override
	protected void onCommand(AbstractCommandSender<T> sender, CommandArguments args) throws CommandFailException {
		this.onCommand(sender.getSender(), (Arguments) args);
	}

	public abstract void onCommand(T sender, Arguments args) throws CommandFailException;
}
