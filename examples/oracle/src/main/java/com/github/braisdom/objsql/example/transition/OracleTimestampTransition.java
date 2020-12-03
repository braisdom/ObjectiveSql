package com.github.braisdom.objsql.example.transition;

import com.github.braisdom.objsql.TableRowAdapter;
import com.github.braisdom.objsql.transition.ColumnTransition;

import java.sql.DatabaseMetaData;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;

import oracle.sql.TIMESTAMP;

public class OracleTimestampTransition implements ColumnTransition {

    @Override
    public Object rising(DatabaseMetaData databaseMetaData,
                         ResultSetMetaData resultSetMetaData, Object object,
                         TableRowAdapter tableRowDescriptor, String fieldName, Object columnValue) throws SQLException {
        if (columnValue != null) {
            TIMESTAMP timestamp = (TIMESTAMP) columnValue;
            return Timestamp.valueOf(timestamp.stringValue());
        } else {
            return null;
        }
    }
}
