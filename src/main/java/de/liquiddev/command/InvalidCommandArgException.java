package de.liquiddev.command;

public class InvalidCommandArgException extends CommandFailException {
	private static final long serialVersionUID = 1L;

	private Class<?> required;
	private String provided;

	public InvalidCommandArgException(CommandNode<?> command, Class<?> required, String provided) {
		super(command);
		this.required = required;
		this.provided = provided;
	}

	public Class<?> getRequired() {
		return required;
	}

	public String getProvided() {
		return provided;
	}

	@Override
	public String getMessage() {
		return "Expected type " + required.getSimpleName() + " but got '" + provided + "'";
	}
}
