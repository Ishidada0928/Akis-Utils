package com.aki.akisutils.apis.util.memory;

@FunctionalInterface
public interface LongLongFunction<T> {

	T apply(long x, long y);

}
