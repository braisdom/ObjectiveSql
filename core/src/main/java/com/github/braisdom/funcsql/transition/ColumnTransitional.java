package com.github.braisdom.funcsql.transition;

import com.github.braisdom.funcsql.DomainModelDescriptor;

import java.sql.DatabaseMetaData;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public interface ColumnTransitional<T> {

    default Object sinking(DatabaseMetaData databaseMetaData,
                           T object, DomainModelDescriptor domainModelDescriptor,
                           String fieldName, Object fieldValue) throws SQLException {
        return fieldValue;
    }

    default Object rising(DatabaseMetaData databaseMetaData, ResultSetMetaData resultSetMetaData,
                          T object, DomainModelDescriptor domainModelDescriptor,
                          String fieldName, Object columnValue) throws SQLException {
        return columnValue;
    }
}
