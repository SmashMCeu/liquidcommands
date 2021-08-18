package de.liquiddev.command;


import org.jetbrains.annotations.Nullable;

import com.google.common.base.Preconditions;

public class InvalidCommandArgException extends CommandFailException {
	private static final long serialVersionUID = 1L;

	private Class<?> required;
	@Nullable
	private String provided;

	public InvalidCommandArgException(CommandNode<?> command, Class<?> required, @Nullable String provided) {
		super(command);
		Preconditions.checkNotNull(required);
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
