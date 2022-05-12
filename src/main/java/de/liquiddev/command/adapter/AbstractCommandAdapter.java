package de.liquiddev.command.adapter;

import java.util.Collections;
import java.util.List;

import com.google.common.base.Preconditions;

import de.liquiddev.command.AbstractCommandSender;
import de.liquiddev.command.CommandArguments;
import de.liquiddev.command.CommandRoot;
import de.liquiddev.command.ErrorReporter;

public abstract class AbstractCommandAdapter<T> {

	private CommandRoot<?> command;

	public AbstractCommandAdapter(CommandRoot<?> command) {
		Preconditions.checkNotNull(command, "command must not be null");
		this.command = command;
	}

	public void onCommand(T sender, String[] args) {
		AbstractCommandSender<T> abstractSender = abstractSender(sender);
		try {
			command.executeCommand(abstractSender, getArguments(args, abstractSender));
		} catch (Exception ex) {
			ErrorReporter reporter = command.getErrorReporter();
			reporter.reportError(this.getClass(), ex, "error executing command: /" + command.getName() + " " + String.join(" ", args));
			abstractSender.sendMessage(command.getPrefix() + "§cThe command execution failed. Please contact an administrator.");
		}
	}

	public List<String> onTabComplete(T sender, String[] args) {
		AbstractCommandSender<T> abstractSender = abstractSender(sender);
		try {
			return command.autocomplete(abstractSender, args);
		} catch (Exception ex) {
			ErrorReporter reporter = command.getErrorReporter();
			reporter.reportError(this.getClass(), ex, "error autocompleting command: /" + command.getName() + " " + String.join(" ", args));
			abstractSender.sendMessage(command.getPrefix() + "§cSomething went wrong. Please contact an administrator.");
			return Collections.emptyList();
		}
	}

	public CommandRoot<?> getCommand() {
		return command;
	}

	public abstract AbstractCommandSender<T> abstractSender(T sender);

	public abstract CommandArguments getArguments(String[] args, AbstractCommandSender<T> sender);
}