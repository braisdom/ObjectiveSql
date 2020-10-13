package com.github.braisdom.objsql;

import com.github.braisdom.objsql.transition.ColumnTransition;

import java.util.Optional;

/**
 * Describes the Java Bean which maps a row in the table, and it
 *
 * @param <T> the bean class
 */
public interface TableRowAdapter<T> {

    String getTableName();

    Class getDomainModelClass();

    T newInstance();

    default void setGeneratedKey(T bean, Object primaryKeyValue) {
        throw new UnsupportedOperationException("The setGeneratedKey is unsupported");
    }

    String getFieldName(String columnName);

    default Optional<String> getFieldDefaultValue(String fieldName) {
        throw new UnsupportedOperationException("The getFieldDefaultValue is unsupported");
    }

    default boolean hasDefaultValue(String fieldName) {
        throw new UnsupportedOperationException("The hasDefaultValue is unsupported");
    }

    default FieldValue getFieldValue(Object bean, String fieldName) {
        throw new UnsupportedOperationException("The getFieldValue is unsupported");
    }

    default Class getFieldType(String fieldName) {
        throw new UnsupportedOperationException("The getFieldType is unsupported");
    }

    default boolean isTransitable(String fieldName) {
        throw new UnsupportedOperationException("The isTransitable is unsupported");
    }

    default ColumnTransition getColumnTransition(String fieldName) {
        throw new UnsupportedOperationException("The getColumnTransition is unsupported");
    }

    default void setFieldValue(T modelObject, String fieldName, Object fieldValue) {
        throw new UnsupportedOperationException("The setFieldValue is unsupported");
    }
}
