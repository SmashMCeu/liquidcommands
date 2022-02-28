package de.liquiddev.command;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Preconditions;

import de.liquiddev.command.autocomplete.Autocompleter;
import de.liquiddev.command.context.CommandContext;
import de.liquiddev.command.example.ReportErrorsCommand;
import de.liquiddev.command.ratelimit.CommandRateLimitExceededException;
import de.liquiddev.util.common.CollectionUtil;

public abstract class CommandRoot<T> extends CommandNode<T> {

	private Autocompleter<T> defaultAutocompleter;
	private CommandFailHandler<T> failHandler;
	private ErrorReporter errorReporter;
	private String prefix;
	private CommandContext context;

	@Deprecated
	public CommandRoot(Class<T> senderType, String name, String hint, String prefix) {
		this(senderType, name, hint);
		this.setPrefix(prefix);
	}

	public CommandRoot(Class<T> senderType, String name, String hint) {
		super(senderType, name, hint);
		this.failHandler = DefaultCommandFailHandler.getDefault();
		this.errorReporter = ErrorReporter.getDefaultReporter();
		this.addSubCommand(new ReportErrorsCommand(this));
		this.context = new CommandContext(this);
		this.prefix = Commands.getDefaultPrefix();
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void executeCommand(AbstractCommandSender sender, CommandArguments args) {
		if (!getSenderType().isInstance(sender.getSender())) {
			this.failHandler.onUnsupportedCommandSender(sender, this, getSenderType());
			return;
		}
		AbstractCommandSender<T> abstractSender = (AbstractCommandSender<T>) sender;
		try {
			super.executeCommand(abstractSender, args);
		} catch (MissingCommandArgException ex) {
			this.failHandler.onMissingArgument(abstractSender, ex.getCommand(), ex.getRequired(), ex.getIndex());
		} catch (InvalidCommandArgException ex) {
			this.failHandler.onInvalidArgument(abstractSender, ex.getCommand(), ex.getRequired(), ex.getProvided());
		} catch (CommandPermissionException ex) {
			this.failHandler.onPermissionFail(abstractSender, ex.getCommand());
		} catch (CommandRateLimitExceededException ex) {
			this.failHandler.onRateLimitExceeded(abstractSender, ex.getCommand());
		} catch (CommandFailException ex) {
			this.failHandler.onCommandFail(abstractSender, ex.getCommand(), ex.getMessage());
		}
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List<String> autocomplete(AbstractCommandSender sender, String[] args) throws IllegalArgumentException {
		if (!getSenderType().isInstance(sender.getSender())) {
			return Collections.emptyList();
		}
		AbstractCommandSender<T> abstractSender = (AbstractCommandSender<T>) sender;
		List<String> suggestions = super.autocomplete(sender, args);

		/* Fallback to default tab completer */
		if (suggestions == null) {
			if (defaultAutocompleter != null) {
				Collection<String> defaultSuggestions = defaultAutocompleter.autocomplete(abstractSender, args[args.length - 1]);
				suggestions = CollectionUtil.toList(defaultSuggestions);
			} else {
				suggestions = Collections.emptyList();
			}
		}
		return suggestions;
	}

	public Autocompleter<T> getDefaultAutocompleter() {
		return this.defaultAutocompleter;
	}

	public void setDefaultAutocompleter(Autocompleter<T> autocompleter) {
		Preconditions.checkNotNull(autocompleter, "autocompleter must not be null");
		this.defaultAutocompleter = autocompleter;
	}

	public CommandFailHandler<T> getFailHandler() {
		return failHandler;
	}

	public void setFailHandler(CommandFailHandler<T> failHandler) {
		Preconditions.checkNotNull(failHandler, "failHandler must not be null");
		this.failHandler = failHandler;
	}

	public ErrorReporter getErrorReporter() {
		return errorReporter;
	}

	public void setErrorReporter(ErrorReporter errorReporter) {
		Preconditions.checkNotNull(errorReporter, "errorReporter must not be null");
		this.errorReporter = errorReporter;
	}

	@Override
	public String getAbsoluteName() {
		return "/" + this.getName();
	}

	@Override
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		Preconditions.checkNotNull(prefix, "prefix must not be null");
		this.prefix = prefix;
	}

	@Override
	protected CommandContext context() {
		return this.context;
	}
}