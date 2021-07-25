package de.liquiddev.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

import de.liquiddev.command.autocomplete.Autocomplete;
import de.liquiddev.command.autocomplete.Autocompleter;
import de.liquiddev.command.context.CommandContext;
import de.liquiddev.command.example.HelpCommand;
import de.liquiddev.command.ratelimit.CommandRateLimitExceededException;
import de.liquiddev.command.ratelimit.RateLimit;
import de.liquiddev.command.ratelimit.RateLimiter;

public abstract class CommandNode<T> {

	private Map<Integer, Collection<CommandChild<? extends T>>> subCommandMap = new HashMap<>();
	private Map<Integer, Autocompleter<? super T>> autocompleters = new HashMap<>(0);

	private Class<T> senderType;
	private String name;
	private String hint;
	private String[] aliases;
	private CommandVisibility visibility;
	private RateLimiter ratelimit;

	@Nullable
	private String permission;
	@Nullable
	private String description;

	public CommandNode(Class<T> senderType, String name, String hint) {
		Preconditions.checkNotNull(senderType, "senderType must not be null");
		Preconditions.checkNotNull(name, "name must not be null");
		Preconditions.checkArgument(!name.isEmpty(), "name must not be empty");
		this.name = name;
		this.hint = hint == null ? "" : hint;
		this.senderType = senderType;
		this.aliases = new String[] {};
		this.visibility = CommandVisibility.SHOW_ALL;
		this.ratelimit = RateLimit.none();
	}

	@SuppressWarnings("rawtypes")
	protected void executeCommand(AbstractCommandSender<T> sender, CommandArguments args) throws CommandFailException {
		if (!this.hasPermission(sender)) {
			throw new CommandPermissionException(this);
		}
		if (!this.ratelimit.acquire(sender.getSender())) {
			throw new CommandRateLimitExceededException(this);
		}

		for (int i = 0; i < args.length(); i++) {
			String qualifier = args.get(i); // the name of the sub command
			CommandChild<? extends T> subCommand = getSubCommand(i, qualifier);
			if (subCommand != null) {
				AbstractCommandSender typelessSender = (AbstractCommandSender) sender;
				Class<? extends T> targetType = subCommand.getSenderType();
				if (!targetType.isInstance(sender.getSender())) {
					throw new CommandSenderException(subCommand, targetType);
				}
				subCommand.executeCommand(typelessSender, args.next(subCommand, i + 1));
				return;
			}
		}
		this.onCommand(sender, args);
	}

	/**
	 * Get the suggestions for the command with the given {@link String} array as
	 * Arguments. The cursor is assumed to be the end of the last argument.
	 * 
	 * Should return <code>null</code> to fallback to the {@link CommandRoot}
	 * default tab {@link Autocompleter}.
	 * 
	 * @param sender
	 * @param args
	 * @return list of suggestions or null to fallback to default suggestions
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("rawtypes")
	@Nullable
	protected List<String> autocomplete(AbstractCommandSender<T> sender, String[] args) throws IllegalArgumentException {
		if (args.length == 0) {
			throw new IllegalArgumentException("Cannot autocomplete empty args");
		}

		if (!this.hasPermission(sender)) {
			return Collections.emptyList();
		}

		List<String> completions = new LinkedList<>();
		int index = args.length - 1;
		String currentArg = args[index].toLowerCase();

		// Autocomplete subcommand names
		if (subCommandMap.containsKey(index)) {
			for (CommandChild<?> child : subCommandMap.get(index)) {
				if (child.isAutocompleteVisible()) {
					if (child.getName().toLowerCase().startsWith(currentArg)) {
						if (child.hasPermission(sender)) {
							completions.add(child.getName());
						}
					}
				}
			}
		}
		// Let the subcommands autocomplete
		else {
			// Find first subcommand to delegate to
			for (int i = 0; i < args.length; i++) {
				String qualifier = args[i];
				CommandChild<? extends T> subCommand = getSubCommand(i, qualifier);
				if (subCommand != null) {
					String[] argsCopy = Arrays.copyOfRange(args, i + 1, args.length);
					AbstractCommandSender typelessSender = (AbstractCommandSender) sender;
					Class<? extends T> targetType = subCommand.getSenderType();
					if (!targetType.isInstance(sender.getSender())) {
						return Collections.emptyList();
					}
					return subCommand.autocomplete(typelessSender, argsCopy);
				}
			}
		}

		/* Return null to fall back to the roots default autocompleter */
		if (this.autocompleters.isEmpty()) {
			return completions.isEmpty() ? null : completions;
		}

		Autocompleter completer = getAutocompleter(index);
		if (completer != null) {
			completions.addAll(completer.autocomplete(sender.getSender(), currentArg));
		}
		return completions;
	}

	/**
	 * Add a tab completer for a given index. Use {@link Autocomplete} for
	 * predefined tab completer.
	 * 
	 * @param index         argument index for the autocompleter
	 * @param autocompleter use {@link Autocomplete}
	 */
	public void setAutocompleter(int index, Autocompleter<? super T> autocompleter) {
		if (autocompleter == null) {
			this.autocompleters.remove(index);
		} else {
			this.autocompleters.put(index, autocompleter);
		}
	}

	private Autocompleter<? super T> getAutocompleter(int index) {
		return autocompleters.get(index);
	}

	public void addSubCommand(CommandChild<? extends T> subCommand) {
		this.addSubCommand(0, subCommand);
	}

	public void addSubCommand(int index, CommandChild<? extends T> subCommand) {
		subCommand.setParent(this);
		Collection<CommandChild<? extends T>> sub = subCommandMap.get(index);
		if (sub == null) {
			sub = new LinkedList<>();
			subCommandMap.put(index, sub);
		}
		sub.add(subCommand);
	}

	@Nullable
	private CommandChild<? extends T> getSubCommand(int index, String name) {
		Collection<CommandChild<? extends T>> sub = subCommandMap.get(index);
		if (sub == null) {
			return null;
		}
		for (CommandChild<? extends T> command : sub) {
			if (command.getName().equalsIgnoreCase(name)) {
				return command;
			}
			for (String alias : command.getAliases()) {
				if (alias.equalsIgnoreCase(name)) {
					return command;
				}
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public String[] getAliases() {
		return aliases;
	}

	public void setAliases(String[] aliases) {
		Preconditions.checkNotNull(aliases, "aliases must not be null");
		this.aliases = aliases;
	}

	public void addAlias(String alias) {
		String[] arr = Arrays.copyOf(aliases, aliases.length + 1);
		arr[arr.length - 1] = alias;
		this.aliases = arr;
	}

	public String getHint() {
		return hint;
	}

	@Nullable
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Nullable
	public String getPermission() {
		return permission;
	}

	public boolean hasPermission(AbstractCommandSender<?> sender) {
		return this.permission == null || sender.hasPermission(permission);
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	/**
	 * Set the command visibility. This affects the autocomplete feature as well as
	 * the {@link HelpCommand} command list.
	 * 
	 * @param visibility the command visibility
	 */
	public void setVisibility(CommandVisibility visibility) {
		Preconditions.checkNotNull(visibility, "visibility must not be null");
		this.visibility = visibility;
	}

	public CommandVisibility getVisibility() {
		return visibility;
	}

	public void setRatelimit(RateLimiter ratelimit) {
		this.ratelimit = ratelimit;
	}

	public RateLimiter getRatelimit() {
		return ratelimit;
	}

	public boolean isAutocompleteVisible() {
		return this.visibility.isShowAutocomplete();
	}

	public boolean isHelpVisible() {
		return this.visibility.isShowInHelp();
	}

	public Class<T> getSenderType() {
		return senderType;
	}

	public Collection<CommandChild<? extends T>> getSubCommands(int index) {
		if (subCommandMap.containsKey(index)) {
			return subCommandMap.get(index).stream().collect(Collectors.toList());
		} else {
			return Collections.emptyList();
		}
	}

	public String getUsage() {
		return this.getAbsoluteName() + (this.hint.length() > 0 ? " " + this.hint : "");
	}

	/**
	 * Adds a context to the commands root. This can be retrieved with getContext()
	 * by all childs of the same root.
	 * 
	 * @param <C>     generic type of the context class
	 * @param type    class type of the context
	 * @param context the context instance
	 * @throws IllegalStateException if context of that type is already registered
	 */
	public <C> void addContext(Class<C> type, C context) throws IllegalStateException {
		this.context().addContext(type, context);
	}

	/**
	 * Retrieve a context by it's class type.
	 * 
	 * @param <C>  generic type of the context class
	 * @param type class type of the context
	 * @return the context instance
	 * @throws IllegalArgumentException if no context of that type is found in the
	 *                                  command root
	 */
	@Nonnull
	public <C> C getContext(Class<C> type) throws IllegalArgumentException {
		return this.context().getContext(type);
	}

	public abstract String getAbsoluteName();

	public abstract String getPrefix();

	protected abstract CommandContext context();

	protected abstract void onCommand(AbstractCommandSender<T> sender, CommandArguments args) throws CommandFailException;
}