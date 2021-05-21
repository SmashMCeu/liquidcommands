package de.liquiddev.command;

public class MissingCommandArgException extends InvalidCommandArgException {
	private static final long serialVersionUID = 1L;

	private int index;

	public MissingCommandArgException(CommandNode<?> command, Class<?> required, int index) {
		super(command, required, null);
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	@Override
	public String getMessage() {
		return "Expected " + getRequired().getSimpleName() + " at index " + index;
	}
}
