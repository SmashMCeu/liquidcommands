package de.liquiddev.command;

import java.util.Arrays;

import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;

import com.google.common.base.Preconditions;

import de.liquiddev.util.common.EnumUtil;

public class CommandArguments {

	public static CommandArguments fromStrings(CommandNode<?> command, String[] args) {
		return new CommandArguments(command, args);
	}

	private final String[] arguments;
	private CommandNode<?> command;
	private int offset;

	protected CommandArguments(CommandNode<?> command, String[] arguments) {
		Preconditions.checkNotNull(command, "command must not be null");
		Preconditions.checkNotNull(arguments, "arguments must not be null");
		this.command = command;
		this.arguments = arguments;
		this.offset = 0;
	}

	/**
	 * Skips to next index.
	 * 
	 * @param next How far should be skipped?
	 */
	protected CommandArguments next(CommandNode<?> nextNode, int next) {
		this.offset += next;
		this.command = nextNode;
		return this;
	}

	/**
	 * Checks if the given index is present.
	 * 
	 * @param index command argument index to be checked
	 * @return <code>true</code> if the index is present and not out of bounds
	 */
	public boolean isPresent(int index) {
		int actualIndex = index + offset;
		if (actualIndex < offset) {
			// For a more simple usage we will ignore the current index as an argument
			actualIndex--;
		}
		return actualIndex >= 0 && actualIndex < arguments.length;
	}

	protected int translateIndex(int index, Class<?> indexType) throws MissingCommandArgException {
		int actualIndex = index + offset;
		if (index < 0) {
			// For a more simple usage we will ignore the current index as an argument
			actualIndex--;
		}
		if (arguments.length <= actualIndex) {
			throw new MissingCommandArgException(command, indexType, index);
		}
		if (actualIndex < 0) {
			// A little bit more complex because we want to ignore the current index as a
			// previous argument, thus needing us to add/substract 1 from min an max.
			int min = -offset + 1;
			int max = arguments.length - offset - 1;
			throw new IndexOutOfBoundsException("Index " + index + " out of bounds [" + min + ", " + max + "]");
		}
		return actualIndex;
	}

	protected CommandNode<?> getCommand() {
		return command;
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
		int actualIndex = translateIndex(index, String.class);
		return arguments[actualIndex];
	}

	public int getInt(int index) throws InvalidCommandArgException {
		int actualIndex = translateIndex(index, Integer.class);
		String arg = arguments[actualIndex];
		try {
			return Integer.parseInt(arg);
		} catch (NumberFormatException ex) {
			throw new InvalidCommandArgException(command, Integer.class, arg);
		}
	}

	public double getDouble(int index) throws InvalidCommandArgException {
		int actualIndex = translateIndex(index, Double.class);
		String arg = arguments[actualIndex];
		try {
			return Double.parseDouble(arg);
		} catch (NumberFormatException ex) {
			throw new InvalidCommandArgException(command, Double.class, arg);
		}
	}

	public <T extends Enum<?>> T getEnum(int index, Class<T> enumType) throws InvalidCommandArgException {
		int actualIndex = translateIndex(index, enumType);
		String name = arguments[actualIndex];
		T[] values = EnumUtil.getValues(enumType);
		for (T t : values) {
			if (t.name().equalsIgnoreCase(name)) {
				return t;
			}
		}
		throw new InvalidCommandArgException(this.command, enumType, name);
	}

	public String join(String delimiter) throws InvalidCommandArgException {
		return join(delimiter, 0);
	}

	public String join(String delimiter, int start) throws InvalidCommandArgException {
		int actualIndex = translateIndex(start, String.class);
		return String.join(delimiter, Arrays.copyOfRange(arguments, actualIndex, arguments.length));
	}

	public String join(int start) throws InvalidCommandArgException {
		return this.join(" ", start);
	}

	public String join() throws InvalidCommandArgException {
		return this.join(" ");
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
			return get(index).equalsIgnoreCase(str);
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
			return get(index).equals(str);
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
		return get(index).equalsIgnoreCase(str);
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
		return get(index).equals(str);
	}

	public String[] toArray() {
		return Arrays.copyOfRange(arguments, offset, arguments.length);
	}

	public int length() {
		return this.arguments.length - offset;
	}

	@Override
	public String toString() {
		return Strings.join(arguments, " ");
	}
}
