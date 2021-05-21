package de.liquiddev.command.adapter;

import java.util.Collections;
import java.util.List;

import de.liquiddev.command.AbstractCommandSender;
import de.liquiddev.command.CommandArguments;
import de.liquiddev.command.CommandRoot;

public abstract class AbstractCommandAdapter<T> {

	private CommandRoot<?> command;

	public AbstractCommandAdapter(CommandRoot<?> command) {
		this.command = command;
	}

	public boolean onCommand(T sender, String[] args) {
		try {
			AbstractCommandSender<T> abstractSender = abstractSender(sender);
			command.executeCommand(abstractSender, getArguments(args));
			return true;
		} catch (Exception ex) {
			if (command.getErrorReporter() != null) {
				command.getErrorReporter().reportError(this.getClass(), ex, "error executing command: /" + command.getName() + " " + String.join(" ", args));
			} else {
				ex.printStackTrace();
			}
			return false;
		}
	}

	public List<String> onTabComplete(T sender, String[] args) {
		try {
			AbstractCommandSender<T> abstractSender = abstractSender(sender);
			return command.autocomplete(abstractSender, args);
		} catch (Exception ex) {
			if (command.getErrorReporter() != null) {
				command.getErrorReporter().reportError(this.getClass(), ex, "error autocompleting command: /" + command.getName() + " " + String.join(" ", args));
			} else {
				ex.printStackTrace();
			}
			return Collections.emptyList();
		}
	}

	public CommandRoot<?> getCommand() {
		return command;
	}

	public abstract AbstractCommandSender<T> abstractSender(T sender);

	public abstract CommandArguments getArguments(String[] args);
}