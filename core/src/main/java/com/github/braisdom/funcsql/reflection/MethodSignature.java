package com.github.braisdom.funcsql.reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

final class MethodSignature implements Comparable<MethodSignature> {

	private final String name;
	private final Class<?> returnType;
	private final Class<?>[] parameterTypes;

	MethodSignature(Method method) {
		this.name = method.getName();
		this.returnType = method.getReturnType();
		this.parameterTypes = method.getParameterTypes();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		MethodSignature that = (MethodSignature) o;
		return Objects.equals(name, that.name) &&
			Objects.equals(returnType, that.returnType) &&
			Arrays.equals(parameterTypes, that.parameterTypes);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, returnType, Arrays.hashCode(parameterTypes));
	}

	String getName() {
		return name;
	}

	Class<?> getReturnType() {
		return returnType;
	}

	private Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	@Override
	public int compareTo(MethodSignature other) {
		Comparator<MethodSignature> comparator = Comparator.comparing(MethodSignature::getName)
			.thenComparing(MethodSignature::getReturnType, Comparator.comparing(Class::getName))
			.thenComparing(MethodSignature::getParameterTypes, Comparator.comparing(MethodSignature::mapToString));
		return comparator.compare(this, other);
	}

	private static String mapToString(Class<?>[] list) {
		return Arrays.stream(list).map(Class::getName).collect(Collectors.joining(", "));
	}

	@Override
	public String toString() {
		return getReturnType().getName() + " " + getName() + "(" + mapToString(getParameterTypes()) + ")";
	}
}
