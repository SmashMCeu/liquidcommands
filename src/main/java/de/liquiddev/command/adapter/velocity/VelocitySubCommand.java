package de.liquiddev.command.adapter.velocity;

import com.velocitypowered.api.command.CommandSource;
import de.liquiddev.command.AbstractCommandSender;
import de.liquiddev.command.CommandArguments;
import de.liquiddev.command.CommandChild;
import de.liquiddev.command.CommandFailException;

public abstract class VelocitySubCommand<T extends CommandSource> extends CommandChild<T> {

	public VelocitySubCommand(Class<T> senderType, String name) {
		this(senderType, name, "");
	}

	public VelocitySubCommand(Class<T> senderType, String name, String hint) {
		super(senderType, name, hint);
	}

	@Override
	protected void onCommand(AbstractCommandSender<T> sender, CommandArguments args) throws CommandFailException {
		VelocityArguments arguments = (VelocityArguments) args;
		this.execute(sender.getSender(), arguments);
	}

	public abstract void execute(T sender, VelocityArguments args) throws CommandFailException;
}