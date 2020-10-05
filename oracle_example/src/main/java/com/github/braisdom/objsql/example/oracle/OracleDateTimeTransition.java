package com.github.braisdom.objsql.example.oracle;

import com.github.braisdom.objsql.TableRowAdapter;
import com.github.braisdom.objsql.transition.ColumnTransition;
import oracle.sql.TIMESTAMP;

import java.sql.DatabaseMetaData;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class OracleDateTimeTransition implements ColumnTransition {
    @Override
    public Object rising(DatabaseMetaData databaseMetaData, ResultSetMetaData resultSetMetaData,
                         Object object, TableRowAdapter tableRowDescriptor,
                         String fieldName, Object columnValue) throws SQLException {
        if(columnValue != null)
            return ((TIMESTAMP)columnValue).timestampValue();
        return null;
    }
}
