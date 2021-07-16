package de.liquiddev.command;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import de.liquiddev.command.CommandArguments;
import de.liquiddev.command.CommandNode;
import de.liquiddev.command.MissingCommandArgException;

public class TestCommandArgument {

	@Test
	public void testIsPresent() {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = CommandArguments.fromStrings(command, new String[] { "test" });
		assertTrue(args.isPresent(0));
		assertFalse(args.isPresent(1));
	}

	@Test
	public void testCheck() throws MissingCommandArgException {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = CommandArguments.fromStrings(command, new String[] { "test" });
		assertTrue(args.check(0, "test"));
	}

	@Test
	public void testCheckIgnoreCase() throws MissingCommandArgException {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = CommandArguments.fromStrings(command, new String[] { "test" });
		assertTrue(args.check(0, "TEST"));
	}

	@Test
	public void testCheckWithCase() throws MissingCommandArgException {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = CommandArguments.fromStrings(command, new String[] { "test" });
		assertFalse(args.checkWithCase(0, "TEST"));
	}

	@Test
	public void testCheckFail() throws MissingCommandArgException {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = CommandArguments.fromStrings(command, new String[] { "test" });
		assertFalse(args.check(0, "no"));
	}

	@Test
	public void testCheckException() throws MissingCommandArgException {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = CommandArguments.fromStrings(command, new String[] {});
		assertThrows(MissingCommandArgException.class, () -> assertFalse(args.check(0, "no")));
	}

	@Test
	public void testCheckOptional() {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = CommandArguments.fromStrings(command, new String[] { "test" });
		assertTrue(args.checkOptional(0, "test"));
	}

	@Test
	public void testCheckOptionalFail() {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = CommandArguments.fromStrings(command, new String[] {});
		assertFalse(args.checkOptional(0, "no"));
	}

	@Test
	public void testCheckOptionalFailIndex() {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = CommandArguments.fromStrings(command, new String[] {});
		assertFalse(args.checkOptional(0, "test"));
	}
}
