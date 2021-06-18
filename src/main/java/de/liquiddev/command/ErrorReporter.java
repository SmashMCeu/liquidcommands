package de.liquiddev.command;

import com.google.common.base.Preconditions;

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

	public abstract void reportError(Class<?> clazz, Exception ex, String string);

	private static class NullReporter extends ErrorReporter {
		@Override
		public String getReportText() {
			return null;
		}

		@Override
		public void reportError(Class<?> clazz, Exception ex, String string) {
			ex.printStackTrace();
		}
	}
}