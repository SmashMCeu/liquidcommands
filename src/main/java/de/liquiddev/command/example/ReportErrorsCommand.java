package de.liquiddev.command.example;

import de.liquiddev.command.AbstractCommandSender;
import de.liquiddev.command.CommandArguments;
import de.liquiddev.command.CommandChild;
import de.liquiddev.command.CommandFailException;
import de.liquiddev.command.CommandRoot;
import de.liquiddev.command.CommandVisibility;
import lombok.Setter;

@SuppressWarnings("rawtypes")
public class ReportErrorsCommand extends CommandChild {

	@Setter
	private CommandRoot<?> commandRoot;

	public ReportErrorsCommand(CommandRoot<?> commandRoot) {
		super(Object.class, "reporterrors", "");
		this.setVisibility(CommandVisibility.HIDDEN);
		this.commandRoot = commandRoot;
	}

	@Override
	protected void onCommand(AbstractCommandSender sender, CommandArguments args) throws CommandFailException {
		sender.sendMessage(commandRoot.getErrorReporter().getReportText());
	}
}