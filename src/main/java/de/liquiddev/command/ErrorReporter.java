package de.liquiddev.command;

import com.google.common.base.Preconditions;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TODO rework this to be a part of command adapter or root.
 *
 */
public abstract class ErrorReporter {
	static ErrorReporter instance = new NullReporter();

	public static ErrorReporter getDefaultReporter() {
		return instance;
	}

	public static void setDefaultReporter(ErrorReporter reporter) {
		Preconditions.checkNotNull(reporter);
		instance = reporter;
	}

	public abstract String getReportText();

	public abstract void reportError(Class<?> clazz, Throwable ex, String string);

	private static class NullReporter extends ErrorReporter {
		@Override
		public String getReportText() {
			return "See console for more information.";
		}

		@Override
		public void reportError(Class<?> clazz, Throwable ex, String string) {
			Logger logger = Logger.getLogger(clazz.getName());
			logger.log(Level.SEVERE, "command execution failed", ex);
		}
	}
}