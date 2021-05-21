package de.liquiddev.command;

public class CommandPermissionException extends CommandFailException {
	private static final long serialVersionUID = 1L;

	public CommandPermissionException(CommandNode<?> command) {
		super(command);
	}

	@Override
	public String getMessage() {
		return "Missing permission " + getCommand().getPermission();
	}
}
