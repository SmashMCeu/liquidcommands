package de.liquiddev.util.common;

import java.lang.reflect.Method;

public class EnumUtil {

	public static <T extends Enum<?>> T[] getValues(Class<T> clazz) {
		try {
			Method method = clazz.getMethod("values");
			T[] values = (T[]) method.invoke(null);
			return values;
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException(ex);
		}
	}
}
