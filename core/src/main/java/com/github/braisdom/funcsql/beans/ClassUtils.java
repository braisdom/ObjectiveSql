package com.github.braisdom.funcsql.beans;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ClassUtils {

	private static final String CGLIB_JAVASSIST_CLASS_SEPARATOR = "$$";
	private static final String BYTE_BUDDY_CLASS_SEPARATOR = "$ByteBuddy$";
	private static final String HIBERNATE_PROXY_CLASS_SEPARATOR = "$HibernateProxy$";

	private static final Map<Class<?>, Set<MethodSignature>> methodsSignaturesCache = new ConcurrentHashMap<>();

	private ClassUtils() {
	}

	public static <T> Class<T> getRealClass(T object) {
		@SuppressWarnings("unchecked")
		Class<T> entityClass = (Class<T>) object.getClass();
		return getRealClass(entityClass);
	}

	public static <T> Class<T> getRealClass(Class<T> clazz) {
		if (isProxyClass(clazz)) {
			if (Proxy.isProxyClass(clazz)) {
				Class<?>[] interfaces = clazz.getInterfaces();
				if (interfaces.length != 1) {
					throw new IllegalArgumentException("Unexpected number of interfaces: " + interfaces.length);
				}
				@SuppressWarnings("unchecked")
				Class<T> proxiedInterface = (Class<T>) interfaces[0];
				return proxiedInterface;
			}
			@SuppressWarnings("unchecked")
			Class<T> superclass = (Class<T>) clazz.getSuperclass();
			return getRealClass(superclass);
		}
		return clazz;
	}

	public static <T> T createNewInstanceLike(T source) {
		if (source == null) {
			return null;
		}

		Class<T> sourceClass = getRealClass(source);
		return createNewInstance(sourceClass);
	}

	public static boolean isFromPackage(Class<?> clazz, String packageName) {
		Package aPackage = clazz.getPackage();
		return aPackage != null && aPackage.getName().equals(packageName);
	}

	@SuppressWarnings(/* this unchecked cast is OK, since this is the contract of newInstance() */"unchecked")
	public static <T> T createNewInstance(Class<T> sourceClass) {
		try {
			Constructor<T> constructor = sourceClass.getDeclaredConstructor();
			return createInstance(constructor);
		} catch (ReflectiveOperationException e) {
			throw new ReflectionException("Failed to construct an instance of " + sourceClass, e);
		}
	}

	static <T> T createInstance(Constructor<T> constructor) throws ReflectiveOperationException {
		boolean accessible = constructor.isAccessible();
		try {
			if (!accessible) {
				constructor.setAccessible(true);
			}
			return constructor.newInstance();
		} finally {
			if (!accessible) {
				constructor.setAccessible(false);
			}
		}
	}

	public static boolean isProxy(Object object) {
		return object != null && isProxyClass(object.getClass());
	}

	public static boolean isProxyClass(Class<?> clazz) {
		if (clazz == null) {
			return false;
		}

		if (Proxy.isProxyClass(clazz)) {
			return true;
		}

		return matchesWellKnownProxyClassNamePattern(clazz.getName());
	}

	static boolean matchesWellKnownProxyClassNamePattern(String className) {
		return className.contains(BYTE_BUDDY_CLASS_SEPARATOR)
			|| className.contains(CGLIB_JAVASSIST_CLASS_SEPARATOR)
			|| className.contains(HIBERNATE_PROXY_CLASS_SEPARATOR);
	}

	public static boolean haveSameSignature(Method oneMethod, Method otherMethod) {
		return new MethodSignature(oneMethod).equals(new MethodSignature(otherMethod));
	}

	public static List<Method> findMethodsByArgumentTypes(Class<?> classToSearchIn, Class<?>... argumentTypes) {
		return Stream.of(classToSearchIn.getMethods())
			.filter(method -> Arrays.equals(method.getParameterTypes(), argumentTypes))
			.collect(Collectors.toList());
	}

	public static boolean hasMethodWithSameSignature(Class<?> clazz, Method method) {
		Set<MethodSignature> methods = methodsSignaturesCache.computeIfAbsent(clazz, ClassUtils::getAllDeclaredMethodSignatures);
		return methods.contains(new MethodSignature(method));
	}

	public static Set<Method> getAllDeclaredMethods(Class<?> clazz) {
		Set<Method> methods = new LinkedHashSet<>(Arrays.asList(clazz.getDeclaredMethods()));
		if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Object.class)) {
			methods.addAll(getAllDeclaredMethods(clazz.getSuperclass()));
		}
		for (Class<?> interfaceClass : clazz.getInterfaces()) {
			methods.addAll(getAllDeclaredMethods(interfaceClass));
		}
		return Collections.unmodifiableSet(methods);
	}

	public static Set<MethodSignature> getAllDeclaredMethodSignatures(Class<?> clazz) {
		return getAllDeclaredMethods(clazz).stream()
			.map(MethodSignature::new)
			.collect(Collectors.toCollection(TreeSet::new));
	}

	public static <A extends Annotation> A findAnnotation(Method method, Class<A> annotationType) {
		A annotation = method.getAnnotation(annotationType);
		if (annotation != null) {
			return annotation;
		}
		return findAnnotation(method.getDeclaringClass(), method, annotationType);
	}

	private static <A extends Annotation> A findAnnotation(Class<?> declaringClass, Method method, Class<A> annotationType) {
		if (declaringClass == null || declaringClass.equals(Object.class)) {
			return null;
		}
		if (declaringClass.getSuperclass() != null) {
			for (Method methodCandidate : declaringClass.getSuperclass().getMethods()) {
				if (isOverride(method, methodCandidate)) {
					A annotation = findAnnotation(methodCandidate, annotationType);
					if (annotation != null) {
						return annotation;
					}
				}
			}
		}
		for (Class<?> interfaceClass : declaringClass.getInterfaces()) {
			for (Method methodCandidate : interfaceClass.getDeclaredMethods()) {
				if (isOverride(method, methodCandidate)) {
					A annotation = findAnnotation(methodCandidate, annotationType);
					if (annotation != null) {
						return annotation;
					}
				}
			}
			A annotation = findAnnotation(interfaceClass, method, annotationType);
			if (annotation != null) {
				return annotation;
			}
		}
		return findAnnotation(declaringClass.getSuperclass(), method, annotationType);
	}

	private static boolean isOverride(Method method, Method candidate) {
		return method.getName().equals(candidate.getName())
			&& candidate.getParameterCount() == method.getParameterCount()
			&& Arrays.equals(candidate.getParameterTypes(), method.getParameterTypes());
	}
}
