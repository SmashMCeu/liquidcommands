package de.liquiddev.command.ratelimit;

import de.liquiddev.command.CommandFailException;
import de.liquiddev.command.CommandNode;

public class CommandRateLimitExceededException extends CommandFailException {

	private static final long serialVersionUID = 1L;

	public CommandRateLimitExceededException(CommandNode<?> command) {
		super(command);
	}
}
