package com.github.braisdom.objsql.transition;

import com.github.braisdom.objsql.TableRowDescriptor;

import java.sql.DatabaseMetaData;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class DefaultColumnTransitional implements ColumnTransitional {

    @Override
    public Object rising(DatabaseMetaData databaseMetaData,
                         ResultSetMetaData resultSetMetaData, Object object,
                         TableRowDescriptor tableRowDescriptor,
                         String fieldName, Object columnValue) throws SQLException {
        Class fieldType = tableRowDescriptor.getFieldType(fieldName);
        Class valueType = columnValue.getClass();



        return null;
    }
}
