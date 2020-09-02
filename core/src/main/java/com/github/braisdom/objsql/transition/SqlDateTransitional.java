package com.github.braisdom.objsql.transition;

import com.github.braisdom.objsql.DomainModelDescriptor;

import java.sql.DatabaseMetaData;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SqlDateTransitional<T> implements ColumnTransitional<T> {

    public static final String DATABASE_TYPE_SQLITE = "SQLite";

    @Override
    public Object sinking(DatabaseMetaData databaseMetaData, T object,
                          DomainModelDescriptor domainModelDescriptor, String fieldName, Object fieldValue) throws SQLException {
        if (fieldValue != null) {
            if (DATABASE_TYPE_SQLITE.equalsIgnoreCase(databaseMetaData.getDatabaseProductName())) {
                return fieldValue.toString();
            } else return fieldValue;
        }
        return null;
    }

    @Override
    public Object rising(DatabaseMetaData databaseMetaData, ResultSetMetaData resultSetMetaData,
                         T object, DomainModelDescriptor domainModelDescriptor, String columnName, Object columnValue) throws SQLException {
        if (columnValue != null) {
            if (DATABASE_TYPE_SQLITE.equalsIgnoreCase(databaseMetaData.getDatabaseProductName())) {
                return Timestamp.valueOf(String.valueOf(columnValue));
            } else return columnValue;
        }
        return null;
    }
}
