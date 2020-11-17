/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.braisdom.objsql.reflection;

import com.github.braisdom.objsql.util.WordUtil;

import java.beans.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class PropertyDescriptorCache<T> {

    private final Class<T> originalClass;
    private final Map<String, PropertyDescriptor> propertyDescriptorsByName = new LinkedHashMap<>();
    private final Map<Field, PropertyDescriptor> propertyDescriptorsByField = new LinkedHashMap<>();
    private final Map<Method, PropertyDescriptor> propertyDescriptorsByMethod = new LinkedHashMap<>();
    private final Map<Class<? extends Annotation>, Map<PropertyDescriptor, Annotation>> propertyDescriptorsByAnnotation = new LinkedHashMap<>();
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
            propertyDescriptorsByAnnotation.computeIfAbsent(annotation.annotationType(), k -> new LinkedHashMap<>()) //
                    .put(propertyDescriptor, annotation);
        }
    }

    private static Collection<PropertyDescriptor> collectAllPropertyDescriptors(Class<?> type) {
        try {
            List<Field> fields = new ArrayList<>();
            detectFields(type, fields);
            Map<String, PropertyDescriptor> propertyDescriptors = new TreeMap<>();
            for (Field field : fields) {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(),
                        getReadMethod(type, field), getWriteMethod(type, field));
                propertyDescriptors.put(propertyDescriptor.getName(), propertyDescriptor);
            }

            collectPropertyDescriptorsOfInterfaces(type, propertyDescriptors);
            return propertyDescriptors.values();
        } catch (IntrospectionException e) {
            throw new ReflectionException(e);
        }
    }

    public static Method getWriteMethod(Class<?> type, Field field) {
        try {
            String writeMethodName = WordUtil.camelize(String.format("set_%s", field.getName()), true);
            return type.getMethod(writeMethodName, field.getType());
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Method getReadMethod(Class<?> type, Field field) {
        try {
            String writeMethodName = WordUtil.camelize(String.format("get_%s", field.getName()), true);
            return type.getMethod(writeMethodName);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static void detectFields(Class<?> type, List<Field> detectedFields) {
        Field[] fields = type.getDeclaredFields();
        detectedFields.addAll(Arrays.asList(fields));
        if (!Object.class.isAssignableFrom(type.getSuperclass())) {
            detectFields(type.getSuperclass(), detectedFields);
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
            throw new ReflectionException("Failed to determine default name for " + PropertyUtils.getQualifiedPropertyName(originalClass, propertyDescriptor), e);
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
