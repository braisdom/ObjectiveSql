package com.github.braisdom.funcsql.beans;

@FunctionalInterface
public interface VoidMethod<T> {
	void invoke(T bean) throws Exception;
}
