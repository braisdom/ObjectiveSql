package com.github.braisdom.funcsql;

public interface DomainModelMetadata {

    String getTableName();

    String[] getColumnNames();

    Object getValue(Object modelObject, String columnName);

    void setValue(Object modelObject, String columnName, Object columnValue);

    ColumnTransition getColumnTransition(String columnName);
}
