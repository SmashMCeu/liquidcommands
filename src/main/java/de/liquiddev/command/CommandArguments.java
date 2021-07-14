package de.liquiddev.command;

import java.util.Arrays;

import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;

import com.google.common.base.Preconditions;

import de.liquiddev.util.common.EnumUtil;

public class CommandArguments {

	public static CommandArguments fromStrings(CommandNode<?> command, String[] args) {
		return new CommandArguments(command, args);
	}

	private String[] arguments;
	private CommandNode<?> command;

	protected CommandArguments(CommandNode<?> command, String[] arguments) {
		Preconditions.checkNotNull(command, "command must not be null");
		Preconditions.checkNotNull(arguments, "arguments must not be null");
		this.command = command;
		this.arguments = arguments;
	}

	/**
	 * Skips to next index and removes the previous.
	 */
	protected CommandArguments next(CommandNode<?> nextNode) {
		this.arguments = Arrays.copyOfRange(arguments, 1, arguments.length);
		this.command = nextNode;
		return this;
	}

	protected CommandNode<?> getCommand() {
		return command;
	}

	public CommandArguments copyOfRange(int from, int to) {
		return CommandArguments.fromStrings(this.command, Arrays.copyOfRange(arguments, from, to));
	}

	/**
	 * {@link Deprecated} use just get() instead.
	 * 
	 * @param index deprecated
	 * @return deprecated
	 * @throws MissingCommandArgException deprecated
	 */
	@Deprecated
	public String getString(int index) throws MissingCommandArgException {
		return get(index);
	}

	public String get(int index) throws MissingCommandArgException {
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

	public <T extends Enum<?>> T getEnum(int index, Class<T> enumType) throws InvalidCommandArgException {
		if (arguments.length <= index) {
			throw new MissingCommandArgException(command, enumType, index);
		}
		String name = arguments[index];
		T[] values = EnumUtil.getValues(enumType);
		for (T t : values) {
			if (t.name().equalsIgnoreCase(name)) {
				return t;
			}
		}
		throw new InvalidCommandArgException(this.command, enumType, name);
	}

	public String getJoinedString(int startIndex) throws InvalidCommandArgException {
		if (arguments.length <= startIndex) {
			throw new MissingCommandArgException(command, String.class, startIndex);
		}

		StringBuilder str = new StringBuilder();
		for (int index = startIndex; index < arguments.length; index++) {
			str.append(arguments[index]);
			str.append(" ");
		}
		return str.toString().trim();
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

	/**
	 * Checks if the given index is present.
	 * 
	 * @param index command argument index to be checked
	 * @return <code>true</code> if the index is present and not out of bounds
	 */
	public boolean isPresent(int index) {
		return index < arguments.length;
	}

	/**
	 * Checks if the given {@link String} is equal to the argument at the given
	 * index. Returns <code>false</code> if either the index is out of bounds or the
	 * given {@link String} does not match to the commands arguments.
	 * 
	 * Note: This is not case sensitive. If you want case sensitivity to matter, use
	 * checkWithCaseOptional() instead.
	 * 
	 * @param index the index to look at
	 * @param str   the {@link String} to be checked
	 * @return <code>true</code> if the {@link String} is present and equal.
	 */
	public boolean checkOptional(int index, String str) {
		if (!isPresent(index)) {
			return false;
		}

		try {
			return getString(index).equalsIgnoreCase(str);
		} catch (InvalidCommandArgException e) {
			// this should never happen, hopefully
			throw new RuntimeException("command index out of bounds: " + index);
		}
	}

	/**
	 * Checks if the given case sensitive {@link String} is equal to the argument at
	 * the given index. Returns <code>false</code> if either the index is out of
	 * bounds or the given {@link String} does not match to the commands arguments.
	 * 
	 * 
	 * @param index the index to look at
	 * @param str   the case sensitive {@link String} to be checked
	 * @return <code>true</code> if the {@link String} is present and equal.
	 */
	public boolean checkWithCaseOptional(int index, String str) {
		if (!isPresent(index)) {
			return false;
		}

		try {
			return getString(index).equals(str);
		} catch (InvalidCommandArgException e) {
			// this should never happen, hopefully
			throw new RuntimeException("command index out of bounds: " + index);
		}
	}

	/**
	 * Checks if the given {@link String} is equal to the argument at the given
	 * index. Returns <code>false</code> if the given {@link String} does not match
	 * the commands arguments. If the index is out of bounds, a
	 * {@link MissingCommandArgException} is thrown.
	 * 
	 * Note: This is not case sensitive. If you want case sensitivity to matter, use
	 * checkWithCase() instead.
	 * 
	 * @param index the index to look at
	 * @param str   the {@link String} to be checked
	 * @return <code>true</code> if the {@link String} is present and equal.
	 * @throws MissingCommandArgException if the index is out of bounds
	 */
	public boolean check(int index, String str) throws MissingCommandArgException {
		return getString(index).equalsIgnoreCase(str);
	}

	/**
	 * Checks if the given case sensitive {@link String} is equal to the argument at
	 * the given index. Returns <code>false</code> if the given {@link String} does
	 * not match the commands arguments. If the index is out of bounds, a
	 * {@link MissingCommandArgException} is thrown.
	 * 
	 * @param index the index to look at
	 * @param str   the case sensitive {@link String} to be checked
	 * @return <code>true</code> if the {@link String} is present and equal.
	 * @throws MissingCommandArgException if the index is out of bounds
	 */
	public boolean checkWithCase(int index, String str) throws MissingCommandArgException {
		return getString(index).equals(str);
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
