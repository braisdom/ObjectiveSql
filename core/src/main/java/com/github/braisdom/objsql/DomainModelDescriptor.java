package com.github.braisdom.objsql;

import com.github.braisdom.objsql.annotations.PrimaryKey;
import com.github.braisdom.objsql.transition.ColumnTransitional;

public interface DomainModelDescriptor<T> {

    T newInstance();

    String getTableName();

    PrimaryKey getPrimaryKey();

    Object getPrimaryValue(T domainObject);

    boolean skipNullOnUpdate();

    Class getDomainModelClass();

    DomainModelDescriptor getRelatedModeDescriptor(Class relatedClass);

    String[] getColumns();

    String[] getInsertableColumns();

    String[] getUpdatableColumns();

    String getColumnName(String fieldName);

    String getFieldName(String columnName);

    Class getFieldType(String fieldName);

    Object getValue(T modelObject, String fieldName);

    void setValue(T modelObject, String fieldName, Object fieldValue);

    ColumnTransitional getColumnTransition(String fieldName);
}
