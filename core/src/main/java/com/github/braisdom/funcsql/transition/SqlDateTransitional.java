package com.github.braisdom.funcsql.transition;

import com.github.braisdom.funcsql.DomainModelDescriptor;

import java.sql.DatabaseMetaData;
import java.sql.ResultSetMetaData;

public class SqlDateTransitional<T> implements ColumnTransitional <T> {

    @Override
    public Object sinking(DatabaseMetaData databaseMetaData, ResultSetMetaData resultSetMetaData,
                          T object, DomainModelDescriptor domainModelDescriptor, String fieldName, Object fieldValue) {
        return null;
    }

    @Override
    public Object rising(DatabaseMetaData databaseMetaData, ResultSetMetaData resultSetMetaData,
                         T object, DomainModelDescriptor domainModelDescriptor, String fieldName, Object fieldValue) {
        return null;
    }
}
