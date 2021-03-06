package de.liquiddev.command;

import com.google.common.base.Preconditions;

import de.liquiddev.command.context.CommandContext;

public abstract class CommandChild<T> extends CommandNode<T> {

	private CommandNode<? super T> parent;

	public CommandChild(Class<T> senderType, String name, String hint) {
		super(senderType, name, hint);
	}

	protected CommandNode<? super T> getParent() {
		return parent;
	}

	protected void setParent(CommandNode<? super T> parent) {
		Preconditions.checkNotNull(parent, "parent must not be null");
		this.parent = parent;
	}

	public CommandRoot<? super T> getRoot() {
		CommandNode<? super T> node = this;
		while (node instanceof CommandChild) {
			node = ((CommandChild<? super T>) node).getParent();
		}
		return (CommandRoot<? super T>) node;
	}

	@Override
	public String getPrefix() {
		return this.parent.getPrefix();
	}

	@Override
	public String getAbsoluteName() {
		return this.parent.getAbsoluteName() + " " + this.getName();
	}

	@Override
	protected CommandContext context() {
		return this.parent.context();
	}
}