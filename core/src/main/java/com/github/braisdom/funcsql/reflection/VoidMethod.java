package com.github.braisdom.funcsql.reflection;

@FunctionalInterface
public interface VoidMethod<T> {
	void invoke(T bean) throws Exception;
}
