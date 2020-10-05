package com.github.braisdom.objsql;

import com.github.braisdom.objsql.reflection.ClassUtils;
import com.github.braisdom.objsql.transition.ColumnTransition;
import com.github.braisdom.objsql.util.WordUtil;

import java.util.Optional;

public class DynamicTableRowDescriptor<T extends DynamicModel> implements TableRowAdapter {

    private final Class<T> clazz;

    public DynamicTableRowDescriptor(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T newInstance() {
        return ClassUtils.createNewInstance(clazz);
    }

    @Override
    public void setGeneratedKey(Object bean, Object primaryKeyValue) {
        throw new UnsupportedOperationException("The dynamic model has no primary key");
    }

    @Override
    public String getFieldName(String columnName) {
        return WordUtil.camelize(columnName, true);
    }

    @Override
    public FieldValue getFieldValue(Object bean, String fieldName) {
        throw new UnsupportedOperationException("Dynamic row descriptor cannot be saved");
    }

    @Override
    public Optional<String> getFieldDefaultValue(String fieldName) {
        return Optional.empty();
    }

    @Override
    public boolean hasDefaultValue(String fieldName) {
        return false;
    }

    @Override
    public Class getFieldType(String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName).getType();
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    @Override
    public boolean isTransitable(String fieldName) {
        return false;
    }

    @Override
    public ColumnTransition getColumnTransition(String fieldName) {
        // The dynamic model need not transit
        return null;
    }

    @Override
    public void setFieldValue(Object modelObject, String fieldName, Object fieldValue) {
        ((DynamicModel)modelObject).put(fieldName, fieldValue);
    }
}
