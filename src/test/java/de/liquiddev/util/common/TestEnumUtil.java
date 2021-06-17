package de.liquiddev.util.common;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class TestEnumUtil {

	static enum ExampleEnum {
		BOB, ALICE;
	}

	@Test
	public void testGetValues() throws Exception {
		ExampleEnum[] values = EnumUtil.getValues(ExampleEnum.class);
		assertTrue(Arrays.deepEquals(ExampleEnum.values(), values));
	}
}