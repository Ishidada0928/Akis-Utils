package com.aki.akisutils.apis.util.matrixutil;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.stream.Stream;

public class UnsafeUtil {

	public static final Unsafe UNSAFE;

	static {
		try {
			Field field = Stream.of(Unsafe.class.getDeclaredFields())
					.filter(f -> f.getType() == Unsafe.class)
					.filter(f -> Modifier.isStatic(f.getModifiers()))
					.filter(f -> Modifier.isFinal(f.getModifiers()))
					.findFirst()
					.orElseThrow(NullPointerException::new);
			field.setAccessible(true);
			UNSAFE = (Unsafe) field.get(null);
		} catch (Exception e) {
			throw new UnsupportedOperationException("Failed to find sun.misc.Unsafe instance");
		}
	}
}
