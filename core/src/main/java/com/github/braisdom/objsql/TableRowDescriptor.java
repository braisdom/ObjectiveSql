package com.github.braisdom.objsql;

import com.github.braisdom.objsql.annotations.PrimaryKey;
import com.github.braisdom.objsql.transition.ColumnTransitional;

public interface TableRowDescriptor<T> {

    T newInstance();

    PrimaryKey getPrimaryKey();

    Object getPrimaryValue(T domainObject);

    String getFieldName(String columnName);

    Class getFieldType(String fieldName);

    ColumnTransitional getColumnTransition(String fieldName);

    void setValue(T modelObject, String fieldName, Object fieldValue);

    Object getValue(T modelObject, String fieldName);
}
