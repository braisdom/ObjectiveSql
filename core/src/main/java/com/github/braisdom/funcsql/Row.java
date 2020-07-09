package com.github.braisdom.funcsql;

public interface Row {

    String[] getColumns();

    int getColumnCount();

    Object getValue(String columnName);

    Boolean getBoolean(String columnName);

    Long getLong(String columnName);

    Integer getInteger(String columnName);

    String getString(String columnName);

    Float getFloat(String columnName);

    Double getDouble(String columnName);

}
