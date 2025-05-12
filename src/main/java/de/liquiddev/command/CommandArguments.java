package de.liquiddev.command;

import de.liquiddev.command.identity.McIdentity;
import de.liquiddev.command.identity.McIdentityToken;
import de.liquiddev.util.common.EnumUtil;
import de.liquiddev.util.common.time.DurationParser;
import lombok.NonNull;

import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;

public class CommandArguments {

	public static CommandArguments fromStrings(CommandNode<?> command, AbstractCommandSender<?> sender, String[] args) {
		return new CommandArguments(command, sender, args);
	}

	private final String[] arguments;
	private CommandNode<?> command;
	private AbstractCommandSender<?> sender;
	private int offset;

	protected CommandArguments(@NonNull CommandNode<?> command, @NonNull AbstractCommandSender<?> sender, @NonNull String[] arguments) {
		this.command = command;
		this.arguments = arguments;
		this.sender = sender;
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
		return actualIndex >= 0 && actualIndex < arguments.length;
	}

	protected int translateIndex(int index, Class<?> indexType) throws MissingCommandArgException {
		int actualIndex = index + offset;
		if (arguments.length <= actualIndex) {
			throw new MissingCommandArgException(command, indexType, index);
		}
		if (actualIndex < 0) {
			int min = -offset;
			int max = arguments.length - offset;
			throw new IndexOutOfBoundsException("Index " + index + " out of bounds [" + min + ", " + max + "]");
		}
		return actualIndex;
	}

	protected CommandNode<?> getCommand() {
		return command;
	}

	public String get(int index) throws MissingCommandArgException {
		int actualIndex = translateIndex(index, String.class);
		return arguments[actualIndex];
	}

	public String getOrElse(int index, String defaultVal) {
		try {
			return isPresent(index) ? get(index) : defaultVal;
		} catch (MissingCommandArgException e) {
			throw new RuntimeException("if you see this, something went horribly wrong", e);
		}
	}

	public int getInt(int index) throws InvalidCommandArgException {
		int actualIndex = translateIndex(index, Integer.class);
		String arg = arguments[actualIndex];
		try {
			int multiplier = 1;
			if (arg.endsWith("k")) {
				multiplier = 1_000;
				arg = arg.substring(0, arg.length() - 1);
			} else if (arg.endsWith("m")) {
				multiplier = 1_000_000;
				arg = arg.substring(0, arg.length() - 1);
			}
			return Integer.parseInt(arg) * multiplier;
		} catch (NumberFormatException ex) {
			throw new InvalidCommandArgException(command, Integer.class, arg);
		}
	}

	public double getDouble(int index) throws InvalidCommandArgException {
		int actualIndex = translateIndex(index, Double.class);
		String arg = arguments[actualIndex].replace(",", ".");
		try {
			int multiplier = 1;
			if (arg.endsWith("k")) {
				multiplier = 1_000;
				arg = arg.substring(0, arg.length() - 1);
			} else if (arg.endsWith("m")) {
				multiplier = 1_000_000;
				arg = arg.substring(0, arg.length() - 1);
			}
			return Double.parseDouble(arg) * multiplier;
		} catch (NumberFormatException ex) {
			throw new InvalidCommandArgException(command, Double.class, arg);
		}
	}

	public boolean getBoolean(int index) throws InvalidCommandArgException {
		int actualIndex = translateIndex(index, Integer.class);
		String arg = arguments[actualIndex];

		final String[] trueValues = {"true", "yes", "on", "enable", "enabled", "1"};
		for (String trueStr : trueValues) {
			if (trueStr.equalsIgnoreCase(arg)) {
				return true;
			}
		}

		final String[] falseValues = {"false", "no", "off", "disable", "disabled", "0"};
		for (String falseStr : falseValues) {
			if (falseStr.equalsIgnoreCase(arg)) {
				return false;
			}
		}

		throw new InvalidCommandArgException(command, Boolean.class, arg);
	}

	public <T extends Enum<?>> T getEnum(int index, Class<T> enumType) throws InvalidCommandArgException {
		int actualIndex = translateIndex(index, enumType);
		String name = arguments[actualIndex];
		T[] values = EnumUtil.getValues(enumType);
		for (T t : values) {
			if (t.name()
					.equalsIgnoreCase(name)) {
				return t;
			}
		}
		throw new InvalidCommandArgException(this.command, enumType, name);
	}

	public Duration getDuration(int index) throws InvalidCommandArgException {
		int actualIndex = translateIndex(index, Double.class);
		String arg = arguments[actualIndex];
		try {
			return DurationParser.parseDuration(arg);
		} catch (IllegalArgumentException ex) {
			throw new InvalidCommandArgException(this.command, Duration.class, arg);
		}
	}

	@SuppressWarnings("rawtypes")
	public McIdentityToken getIdentity(int index) throws InvalidCommandArgException {
		int actualIndex = translateIndex(index, McIdentity.class);
		String arg = arguments[actualIndex];
		McIdentityToken token;
		try {
			token = McIdentityToken.ofUuid(UUID.fromString(arg));
		} catch (IllegalArgumentException ex) {
			token = McIdentityToken.ofName(arg);
		}
		token.whenUnknown(unknown -> getFailHandler().onInvalidArgument((AbstractCommandSender) sender, (CommandNode) command, McIdentity.class, arg));
		return token;
	}

	private CommandFailHandler<?> getFailHandler() {
		if (command instanceof CommandRoot<?>) {
			return ((CommandRoot<?>) command).getFailHandler();
		} else if (command instanceof CommandChild<?>) {
			return ((CommandChild<?>) command).getRoot()
					.getFailHandler();
		} else {
			throw new IllegalStateException();
		}
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
	 * <p>
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
	 * <p>
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

	public boolean isEmpty() {
		return length() == 0;
	}

	@Override
	public String toString() {
		return String.join(" ", arguments);
	}
}
