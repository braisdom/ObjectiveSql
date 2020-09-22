package com.github.braisdom.objsql;

import com.github.braisdom.objsql.annotations.PrimaryKey;
import com.github.braisdom.objsql.reflection.ClassUtils;
import com.github.braisdom.objsql.reflection.PropertyUtils;
import com.github.braisdom.objsql.transition.ColumnTransitional;

public class DynamicTableRowDescriptor<T> implements TableRowDescriptor<T> {

    private final Class<T> clazz;

    public DynamicTableRowDescriptor(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T newInstance() {
        return ClassUtils.createNewInstance(clazz);
    }

    @Override
    public PrimaryKey getPrimaryKey() {
        return null;
    }

    @Override
    public Object getPrimaryValue(T domainObject) {
        return null;
    }

    @Override
    public String getFieldName(String columnName) {
        return columnName;
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
    public ColumnTransitional getColumnTransition(String fieldName) {
        return null;
    }

    @Override
    public void setValue(T modelObject, String fieldName, Object fieldValue) {
        PropertyUtils.write(modelObject, fieldName, fieldValue);
    }

    @Override
    public Object getValue(T modelObject, String fieldName) {
        return PropertyUtils.read(modelObject, fieldName);
    }
}
