package com.github.braisdom.objsql;

import com.github.braisdom.objsql.util.FunctionWithThrowable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.braisdom.objsql.DatabaseType.*;

public class DefaultQuoter implements Quoter {

    @Override
    public String quoteTableName(String databaseProductName, String tableName) {
        String[] tableNameItems = tableName.split("\\.");
        String[] quotedTableNames = Arrays.stream(tableNameItems).map(FunctionWithThrowable
                .castFunctionWithThrowable(item -> quoteName(databaseProductName, item))).toArray(String[]::new);
        return String.join(".", quotedTableNames);
    }

    @Override
    public String quoteColumnName(String databaseProductName, String columnName) {
        return quoteName(databaseProductName, columnName);
    }

    @Override
    public String[] quoteColumnNames(String databaseProductName, String[] columnNames) {
        String[] quotedColumnNames = Arrays.stream(columnNames).map(FunctionWithThrowable
                .castFunctionWithThrowable(column -> quoteName(databaseProductName, column))).toArray(String[]::new);
        return quotedColumnNames;
    }

    @Override
    public String[] quoteValues(String databaseProductName, Object... values) {
        List<Object> quotedValues = new ArrayList<>();

        for (Object value : values) {
            quotedValues.add(quoteValue(value));
        }

        return quotedValues.toArray(new String[0]);
    }

    @Override
    public String quoteValue(Object value) {
        if (value instanceof Integer || value instanceof Long ||
                value instanceof Float || value instanceof Double) {
            return String.valueOf(value);
        }

        return new StringBuffer().append("'").append(value).append("'").toString();
    }

    protected String quoteName(String databaseProductName, String item) {
        StringBuilder sb = new StringBuilder();
        if (MySQL.equals(databaseProductName)) {
            return sb.append("`").append(item).append("`").toString();
        } else if (PostgreSQL.equals(databaseProductName)) {
            return sb.append("\"").append(item).append("\"").toString();
        } else if(Oracle.equals(databaseProductName)) {
            return sb.append("\"").append(item.toUpperCase()).append("\"").toString();
        } else if(HSQLDB.equals(databaseProductName)) {
            return String.valueOf(item);
        }

        return sb.append("\"").append(item).append("\"").toString();
    }
}
