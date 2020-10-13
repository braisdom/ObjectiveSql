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
    public Class getDomainModelClass() {
        return clazz;
    }

    @Override
    public String getTableName() {
        return Tables.getTableName(clazz);
    }

    @Override
    public T newInstance() {
        return ClassUtils.createNewInstance(clazz);
    }

    @Override
    public String getFieldName(String columnName) {
        return WordUtil.camelize(columnName, true);
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
    public void setFieldValue(Object modelObject, String fieldName, Object fieldValue) {
        ((DynamicModel)modelObject).put(fieldName, fieldValue);
    }
}
