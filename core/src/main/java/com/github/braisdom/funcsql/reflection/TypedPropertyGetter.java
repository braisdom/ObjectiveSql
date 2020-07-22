package com.github.braisdom.funcsql.reflection;

@FunctionalInterface
public interface TypedPropertyGetter<T, V> {
	V get(T bean);
}
