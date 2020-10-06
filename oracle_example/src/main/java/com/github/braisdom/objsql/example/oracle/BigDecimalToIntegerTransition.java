package com.github.braisdom.objsql.example.oracle;

import com.github.braisdom.objsql.TableRowAdapter;
import com.github.braisdom.objsql.transition.ColumnTransition;

import java.math.BigDecimal;
import java.sql.DatabaseMetaData;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class BigDecimalToIntegerTransition implements ColumnTransition {

    @Override
    public Object rising(DatabaseMetaData databaseMetaData, ResultSetMetaData resultSetMetaData,
                         Object object, TableRowAdapter tableRowDescriptor, String fieldName,
                         Object columnValue) throws SQLException {
        if(columnValue instanceof BigDecimal)
            return ((BigDecimal)columnValue).intValue();
        return columnValue;
    }
}
