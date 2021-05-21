package de.liquiddev.command;

import java.util.Arrays;

import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;

public class CommandArguments {

	public static CommandArguments fromStrings(CommandNode<?> command, String[] args) {
		return new CommandArguments(command, args);
	}

	private String[] arguments;
	private CommandNode<?> command;

	protected CommandArguments(CommandNode<?> command, String[] arguments) {
		this.command = command;
		this.arguments = arguments;
	}

	public String getString(int index) throws InvalidCommandArgException {
		if (arguments.length <= index) {
			throw new MissingCommandArgException(command, String.class, index);
		}
		return arguments[index];
	}

	public int getInt(int index) throws InvalidCommandArgException {
		if (arguments.length <= index) {
			throw new MissingCommandArgException(command, Integer.class, index);
		}
		String arg = arguments[index];
		try {
			return Integer.parseInt(arg);
		} catch (NumberFormatException ex) {
			throw new InvalidCommandArgException(command, Integer.class, arg);
		}
	}

	public double getDouble(int index) throws InvalidCommandArgException {
		if (arguments.length <= index) {
			throw new MissingCommandArgException(command, Double.class, index);
		}
		String arg = arguments[index];
		try {
			return Double.parseDouble(arg);
		} catch (NumberFormatException ex) {
			throw new InvalidCommandArgException(command, Double.class, arg);
		}
	}

	protected CommandNode<?> getCommand() {
		return command;
	}

	/**
	 * Skips to next index and removes the previous.
	 */
	protected CommandArguments next(CommandNode<?> nextNode) {
		this.arguments = Arrays.copyOfRange(arguments, 1, arguments.length);
		this.command = nextNode;
		return this;
	}

	public CommandArguments copyOfRange(int from, int to) {
		return CommandArguments.fromStrings(this.command, Arrays.copyOfRange(arguments, from, to));
	}

	public String join(String delimiter) throws InvalidCommandArgException {
		if (arguments.length == 0) {
			throw new MissingCommandArgException(command, String.class, 0);
		}
		return String.join(delimiter, arguments);
	}

	public String join(String delimiter, int from) throws InvalidCommandArgException {
		if (arguments.length <= from) {
			throw new MissingCommandArgException(command, String.class, from);
		}
		return String.join(delimiter, Arrays.copyOfRange(arguments, from, arguments.length));
	}

	public String join(int from) throws InvalidCommandArgException {
		return this.join(" ", from);
	}

	public String join() throws InvalidCommandArgException {
		return this.join(" ");
	}

	protected String[] getArguments() {
		return arguments;
	}

	public int length() {
		return this.arguments.length;
	}

	@Override
	public String toString() {
		return Strings.join(arguments, " ");
	}
}
