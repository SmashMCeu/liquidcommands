package de.liquiddev.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

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

	@Test
	public void testNext() {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = CommandArguments.fromStrings(command, new String[] { "arg1", "arg2" });
		args.next(command);
		assertEquals(1, args.length());
	}

	@Test
	public void testPrevArgs() throws MissingCommandArgException {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = CommandArguments.fromStrings(command, new String[] { "arg1", "arg2" });
		args.next(command);
		assertTrue(args.isPresent(-1));
		assertEquals("arg1", args.get(-1));
	}

	@Test
	public void testPrevArgsOutOfBounds() throws MissingCommandArgException {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = CommandArguments.fromStrings(command, new String[] { "arg1", "arg2" });
		args.next(command);
		assertThrows(IndexOutOfBoundsException.class, () -> args.get(-2));
	}

	@Test
	public void testCheckOptionalPrevArgs() throws MissingCommandArgException {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = CommandArguments.fromStrings(command, new String[] { "arg1", "arg2" });
		args.next(command);
		assertTrue(args.checkOptional(-1, "arg1"));
	}

	@Test
	public void testCheckOptionalPrevArgsNegative() throws MissingCommandArgException {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = CommandArguments.fromStrings(command, new String[] { "arg1", "arg2" });
		args.next(command);
		assertFalse(args.checkOptional(-1, "arg2"));
	}

	@Test
	public void testCheckOptionalPrevArgsOutOfBounds() throws MissingCommandArgException {
		CommandNode<?> command = mock(CommandNode.class);
		CommandArguments args = CommandArguments.fromStrings(command, new String[] { "arg1", "arg2" });
		args.next(command);
		assertFalse(args.checkOptional(-2, "arg1"));
	}
}
