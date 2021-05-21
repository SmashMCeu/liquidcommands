package de.liquiddev.command;

public class CommandFailException extends Exception {
	private static final long serialVersionUID = 1L;

	private CommandNode<?> command;
	private String reason;

	public CommandFailException(CommandNode<?> command) {
		this.command = command;
	}

	public CommandFailException(CommandNode<?> command, String reason) {
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
