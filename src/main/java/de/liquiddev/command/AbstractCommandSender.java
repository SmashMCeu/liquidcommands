package de.liquiddev.command;

import com.google.common.base.Preconditions;

public abstract class AbstractCommandSender<T> {
	protected T sender;

	public AbstractCommandSender(T sender) {
		Preconditions.checkNotNull(sender, "sender must not be null");
		this.sender = sender;
	}

	public T getSender() {
		return sender;
	}

	public abstract void sendMessage(String message);

	public abstract boolean hasPermission(String permission);

	public abstract String getName();
}