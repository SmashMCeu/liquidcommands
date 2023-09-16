package de.liquiddev.command.adapter.bukkit;

import de.liquiddev.command.AbstractCommandSender;
import de.liquiddev.command.CommandArguments;
import de.liquiddev.command.CommandChild;
import de.liquiddev.command.CommandFailException;
import org.bukkit.command.CommandSender;

abstract class SubCommand<T extends CommandSender> extends CommandChild<T> {

	public SubCommand(Class<T> senderType, String name) {
		this(senderType, name, "");
	}

	public SubCommand(Class<T> senderType, String name, String hint) {
		super(senderType, name, hint);
	}

	@Override
	protected void onCommand(AbstractCommandSender<T> sender, CommandArguments args) throws CommandFailException {
		this.onCommand(sender.getSender(), (Arguments) args);
	}

	public abstract void onCommand(T sender, Arguments args) throws CommandFailException;
}
