package com.github.braisdom.objsql;

import com.github.braisdom.objsql.transition.ColumnTransitional;

import java.util.Objects;
import java.util.Optional;

/**
 * Describes the Java Bean which maps a row in the table, and it
 *
 * @param <T> the bean class
 */
public interface TableRowAdapter<T> {

    T newInstance();

    void setGeneratedKey(T bean, Object primaryKeyValue);

    String getFieldName(String columnName);

    Optional<String> getFieldDefaultValue(String fieldName);

    boolean hasDefaultValue(String fieldName);

    FieldValue getFieldValue(Object bean, String fieldName);

    Class getFieldType(String fieldName);

    boolean isTransitable(String fieldName);

    ColumnTransitional getColumnTransition(String fieldName);

    void setFieldValue(T modelObject, String fieldName, Object fieldValue);
}
