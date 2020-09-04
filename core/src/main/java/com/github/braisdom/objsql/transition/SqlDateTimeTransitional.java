package com.github.braisdom.objsql.transition;

import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.DomainModelDescriptor;

import java.sql.DatabaseMetaData;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SqlDateTimeTransitional<T> implements ColumnTransitional<T> {

    @Override
    public Object sinking(DatabaseMetaData databaseMetaData, T object,
                          DomainModelDescriptor domainModelDescriptor, String fieldName, Object fieldValue) throws SQLException {
        if (fieldValue != null) {
            if (DatabaseType.SQLite.nameEquals(databaseMetaData.getDatabaseProductName())) {
                return fieldValue.toString();
            } else return fieldValue.toString();
        }
        return null;
    }

    @Override
    public Object rising(DatabaseMetaData databaseMetaData, ResultSetMetaData resultSetMetaData,
                         T object, DomainModelDescriptor domainModelDescriptor, String columnName, Object columnValue) throws SQLException {
        if (columnValue != null) {
            if (DatabaseType.SQLite.nameEquals(databaseMetaData.getDatabaseProductName())) {
                return Timestamp.valueOf(String.valueOf(columnValue));
            } else return columnValue;
        }
        return null;
    }
}
