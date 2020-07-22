package com.github.braisdom.funcsql.beans;

@FunctionalInterface
public interface TypedPropertyGetter<T, V> {
	V get(T bean);
}
