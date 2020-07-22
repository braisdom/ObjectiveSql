package com.github.braisdom.funcsql.reflection;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

class PropertyDescriptorCache<T> {

	private final Class<T> originalClass;
	private final AtomicReference<Class<? extends T>> methodCapturingProxy = new AtomicReference<>();
	private final Map<String, PropertyDescriptor> propertyDescriptorsByName = new LinkedHashMap<>();
	private final Map<Field, PropertyDescriptor> propertyDescriptorsByField = new LinkedHashMap<>();
	private final Map<Method, PropertyDescriptor> propertyDescriptorsByMethod = new LinkedHashMap<>();
	private final Map<Class<? extends Annotation>, Map<PropertyDescriptor, Annotation>> propertyDescriptorsByAnnotation = new LinkedHashMap<>();
	private final Map<TypedPropertyGetter<T, ?>, Method> methodByPropertyGetterCache = new ConcurrentHashMap<>();
	private final Map<VoidMethod<T>, Method> methodByVoidMethodCache = new ConcurrentHashMap<>();
	private final Map<PropertyDescriptor, Object> defaultValues = new ConcurrentHashMap<>();

	private PropertyDescriptorCache(Class<T> originalClass) {
		this.originalClass = originalClass;

		for (PropertyDescriptor propertyDescriptor : getAllPropertyDescriptors()) {
			PropertyDescriptor existing = propertyDescriptorsByName.putIfAbsent(propertyDescriptor.getName(), propertyDescriptor);

			Method readMethod = propertyDescriptor.getReadMethod();
			if (readMethod != null) {
				propertyDescriptorsByMethod.put(readMethod, propertyDescriptor);
				putAnnotations(propertyDescriptor, readMethod.getAnnotations());
			}

			Method writeMethod = propertyDescriptor.getWriteMethod();
			if (writeMethod != null) {
				propertyDescriptorsByMethod.put(writeMethod, propertyDescriptor);
				putAnnotations(propertyDescriptor, writeMethod.getAnnotations());
			}
		}

		for (Field field : getFields()) {
			PropertyDescriptor propertyDescriptor = propertyDescriptorsByName.get(field.getName());
			if (propertyDescriptor != null) {
				PropertyDescriptor existing = propertyDescriptorsByField.putIfAbsent(field, propertyDescriptor);
				putAnnotations(propertyDescriptor, field.getAnnotations());
			}
		}
	}

	private Set<Field> getFields() {
		List<Field> allFields = new ArrayList<>();
		collectFields(originalClass, allFields);
		allFields.sort(Comparator.comparing(Field::getName));
		return new LinkedHashSet<>(allFields);
	}

	private static void collectFields(Class<?> type, Collection<Field> collectedFields) {
		collectedFields.addAll(Arrays.asList(type.getFields()));
		collectedFields.addAll(Arrays.asList(type.getDeclaredFields()));
		if (!type.equals(Object.class)) {
			Class<?> superclass = type.getSuperclass();
			if (superclass != null) {
				collectFields(superclass, collectedFields);
			}
		}
	}

	private void putAnnotations(PropertyDescriptor propertyDescriptor, Annotation[] annotations) {
		for (Annotation annotation : annotations) {
			propertyDescriptorsByAnnotation.computeIfAbsent(annotation.annotationType(),k -> new LinkedHashMap<>()) //
				.put(propertyDescriptor, annotation);
		}
	}

	private static Collection<PropertyDescriptor> collectAllPropertyDescriptors(Class<?> type) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(type);
			Map<String, PropertyDescriptor> propertyDescriptors = new TreeMap<>();
			for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
				propertyDescriptors.put(propertyDescriptor.getName(), propertyDescriptor);
			}

			collectPropertyDescriptorsOfInterfaces(type, propertyDescriptors);
			return propertyDescriptors.values();
		} catch (IntrospectionException e) {
			throw new ReflectionException(e);
		}
	}

	// workaround for https://bugs.openjdk.java.net/browse/JDK-8071693
	private static void collectPropertyDescriptorsOfInterfaces(Class<?> type, Map<String, PropertyDescriptor> propertyDescriptors)
			throws IntrospectionException {
		if (type == null || type.equals(Object.class)) {
			return;
		}
		for (Class<?> typeInterface : type.getInterfaces()) {
			BeanInfo beanInfo = Introspector.getBeanInfo(typeInterface);
			for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
				propertyDescriptors.putIfAbsent(propertyDescriptor.getName(), propertyDescriptor);
			}
			collectPropertyDescriptorsOfInterfaces(typeInterface, propertyDescriptors);
		}
		collectPropertyDescriptorsOfInterfaces(type.getSuperclass(), propertyDescriptors);
	}

	private Collection<PropertyDescriptor> getAllPropertyDescriptors() {
		return collectAllPropertyDescriptors(originalClass);
	}

	Collection<PropertyDescriptor> getDescriptors() {
		return Collections.unmodifiableCollection(propertyDescriptorsByName.values());
	}

	PropertyDescriptor getDescriptorByMethod(Method method) {
		return propertyDescriptorsByMethod.get(method);
	}

	PropertyDescriptor getDescriptorByField(Field field) {
		return propertyDescriptorsByField.get(field);
	}

	<A extends Annotation> Map<PropertyDescriptor, A> getDescriptorsForAnnotation(Class<A> annotationClass) {
		@SuppressWarnings("unchecked")
		Map<PropertyDescriptor, A> descriptors = (Map<PropertyDescriptor, A>) propertyDescriptorsByAnnotation.getOrDefault(
			annotationClass, Collections.emptyMap());
		return Collections.unmodifiableMap(descriptors);
	}

	static <T> PropertyDescriptorCache<T> compute(Class<T> originalClass) {
		return new PropertyDescriptorCache<>(originalClass);
	}

	PropertyDescriptor getDescriptorByName(String propertyName) {
		return propertyDescriptorsByName.get(propertyName);
	}

	Object getDefaultValue(PropertyDescriptor propertyDescriptor) {
		return defaultValues.computeIfAbsent(propertyDescriptor, this::determineDefaultValue);
	}

	private Object determineDefaultValue(PropertyDescriptor propertyDescriptor) {
		try {
			Object defaultObject = ClassUtils.createNewInstance(originalClass);
			return PropertyUtils.read(defaultObject, propertyDescriptor);
		} catch (RuntimeException e) {
			throw new ReflectionException("Failed to determine default value for " + PropertyUtils.getQualifiedPropertyName(originalClass, propertyDescriptor), e);
		}
	}

	private static void assertHasNoDeclaredFields(Object lambda) {
		if (hasDeclaredFields(lambda)) {
			throw new IllegalArgumentException(lambda + " is call site specific");
		}
	}

	private static boolean hasDeclaredFields(Object lambda) {
		return lambda.getClass().getDeclaredFields().length > 0;
	}

}
