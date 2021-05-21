package de.liquiddev.util.common;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CollectionUtil {
	public static <T> List<T> toList(Collection<T> collection) {
		return collection instanceof List ? (List<T>) collection : Arrays.asList(collection.toArray((T[]) new Object[collection.size()]));
	}
}