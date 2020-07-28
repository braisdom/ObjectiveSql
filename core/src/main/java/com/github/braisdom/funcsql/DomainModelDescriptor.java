package com.github.braisdom.funcsql;

public interface DomainModelDescriptor {

    String getTableName();

    String getPrimaryKey();

    String[] getColumnNames(boolean insertable, boolean updatable);

    Object getValue(Object modelObject, String columnName);

    void setValue(Object modelObject, String columnName, Object columnValue);

    ColumnTransition getColumnTransition(String columnName);
}
