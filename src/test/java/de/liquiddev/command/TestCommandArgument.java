package de.liquiddev.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

public class TestCommandArgument {

	static CommandArguments argumentsOf(CommandNode<?> command, String... strings) {
		AbstractCommandSender<?> sender = mock(AbstractCommandSender.class);
		return CommandArguments.fromStrings(command, sender, strings);
	}

	@Test
	public void testIsPresent() {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = argumentsOf(command, "test");
		assertTrue(args.isPresent(0));
		assertFalse(args.isPresent(1));
	}

	@Test
	public void testCheck() throws MissingCommandArgException {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = argumentsOf(command, "test");
		assertTrue(args.check(0, "test"));
	}

	@Test
	public void testCheckIgnoreCase() throws MissingCommandArgException {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = argumentsOf(command, "test");
		assertTrue(args.check(0, "TEST"));
	}

	@Test
	public void testCheckWithCase() throws MissingCommandArgException {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = argumentsOf(command, "test");
		assertFalse(args.checkWithCase(0, "TEST"));
	}

	@Test
	public void testCheckFail() throws MissingCommandArgException {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = argumentsOf(command, "test");
		assertFalse(args.check(0, "no"));
	}

	@Test
	public void testCheckException() throws MissingCommandArgException {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = argumentsOf(command);
		assertThrows(MissingCommandArgException.class, () -> assertFalse(args.check(0, "no")));
	}

	@Test
	public void testCheckOptional() {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = argumentsOf(command, "test");
		assertTrue(args.checkOptional(0, "test"));
	}

	@Test
	public void testCheckOptionalFail() {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = argumentsOf(command, "test");
		assertFalse(args.checkOptional(0, "no"));
	}

	@Test
	public void testCheckOptionalFailIndex() {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = argumentsOf(command);
		assertFalse(args.checkOptional(0, "test"));
	}

	@Test
	public void testNext() {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = argumentsOf(command, "prevarg", "command", "arg0");
		args.next(command, 2);
		assertEquals(1, args.length());
	}

	@Test
	public void testPrevArgs() throws MissingCommandArgException {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = argumentsOf(command, "prevarg", "command", "arg0");
		args.next(command, 2);
		assertTrue(args.isPresent(-1));
		assertEquals("prevarg", args.get(-1));
	}

	@Test
	public void testPrevArgsOutOfBounds() throws MissingCommandArgException {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = argumentsOf(command, "prevarg", "command", "arg0");
		args.next(command, 1);
		assertThrows(IndexOutOfBoundsException.class, () -> args.get(-2));
	}

	@Test
	public void testCheckOptionalPrevArgs() throws MissingCommandArgException {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = argumentsOf(command, "prevarg", "commandname", "arg0");
		args.next(command, 2);
		assertTrue(args.checkOptional(-1, "prevarg"));
	}

	@Test
	public void testCheckOptionalPrevArgsNegative() throws MissingCommandArgException {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = argumentsOf(command, "prevarg", "commandname", "arg0");
		args.next(command, 2);
		assertFalse(args.check(-1, "commandname"));
	}

	@Test
	public void testCheckOptionalPrevArgsOutOfBounds() throws MissingCommandArgException {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = argumentsOf(command, "prevarg", "commandname", "arg0");
		args.next(command, 2);
		assertFalse(args.checkOptional(-2, "prevargs"));
	}
}
