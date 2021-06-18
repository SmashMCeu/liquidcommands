package de.liquiddev.command;

import com.google.common.base.Preconditions;

public class CommandSenderException extends CommandFailException {

	private static final long serialVersionUID = 1L;

	private Class<?> requiredType;

	public CommandSenderException(CommandNode<?> command, Class<?> requiredType) {
		super(command);
		Preconditions.checkNotNull(requiredType, "requiredType must not be null");
		this.requiredType = requiredType;
	}

	public Class<?> getRequiredType() {
		return requiredType;
	}

	@Override
	public String getMessage() {
		return "Unsupported command sender. Required type " + requiredType.getSimpleName();
	}
}
