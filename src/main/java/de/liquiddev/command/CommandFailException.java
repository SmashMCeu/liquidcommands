package de.liquiddev.command;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

public class CommandFailException extends Exception {
	private static final long serialVersionUID = 1L;

	private CommandNode<?> command;

	@Nullable
	private String reason;

	public CommandFailException(CommandNode<?> command) {
		this(command, null);
	}

	public CommandFailException(CommandNode<?> command, String reason) {
		Preconditions.checkNotNull(command, "command must not be null");
		this.command = command;
		this.reason = reason;
	}

	public <T> CommandNode<T> getCommand() {
		return (CommandNode<T>) command;
	}

	@Override
	public String getMessage() {
		return reason == null ? "Failed executing command" : reason;
	}
}
