package de.liquiddev.command;

public abstract class AbstractCommandSender<T> {
	protected T sender;

	public AbstractCommandSender(T sender) {
		this.sender = sender;
	}

	public T getSender() {
		return sender;
	}

	public abstract void sendMessage(String message);

	public abstract boolean hasPermission(String permission);

	public abstract String getName();
}