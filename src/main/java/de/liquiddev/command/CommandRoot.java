package de.liquiddev.command;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.liquiddev.command.autocomplete.Autocompleter;
import de.liquiddev.command.example.ReportErrorsCommand;
import de.liquiddev.util.common.CollectionUtil;

public abstract class CommandRoot<T> extends CommandNode<T> {

	private Autocompleter<T> defaultCompleter;
	private CommandFailHandler<T> failHandler;
	private ErrorReporter errorReporter;
	private String prefix;

	public CommandRoot(Class<T> senderType, String name, String hint) {
		this(senderType, name, hint, "");
	}

	public CommandRoot(Class<T> senderType, String name, String hint, String prefix) {
		super(senderType, name, hint);
		this.prefix = prefix;
		this.failHandler = DefaultCommandFailHandler.getDefault();
		this.errorReporter = ErrorReporter.getDefaultReporter();
		this.addSubCommand(new ReportErrorsCommand(this));
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void executeCommand(AbstractCommandSender sender, CommandArguments args) {
		if (!getSenderType().isInstance(sender.getSender())) {
			this.failHandler.onUnsupportedCommandSender(sender, getSenderType());
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
			if (defaultCompleter != null) {
				Collection<String> defaultSuggestions = defaultCompleter.autocomplete(abstractSender.getSender(), args[args.length - 1]);
				suggestions = CollectionUtil.toList(defaultSuggestions);
			} else {
				suggestions = Collections.emptyList();
			}
		}
		return suggestions;
	}

	public Autocompleter<T> getDefaultAutocompleter() {
		return this.defaultCompleter;
	}

	public void setDefaultAutocompleter(Autocompleter<T> completable) {
		this.defaultCompleter = completable;
	}

	public CommandFailHandler<T> getFailHandler() {
		return failHandler;
	}

	public void setFailHandler(CommandFailHandler<T> failHandler) {
		this.failHandler = failHandler;
	}

	public void setErrorReporter(ErrorReporter errorReporter) {
		this.errorReporter = errorReporter;
	}

	public ErrorReporter getErrorReporter() {
		return errorReporter;
	}

	@Override
	public String getAbsoluteName() {
		return "/" + this.getName();
	}

	@Override
	public String getPrefix() {
		return prefix;
	}
}