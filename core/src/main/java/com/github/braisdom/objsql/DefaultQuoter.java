package com.github.braisdom.objsql;

import com.github.braisdom.objsql.util.FunctionWithThrowable;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.braisdom.objsql.DatabaseType.*;

public class DefaultQuoter implements Quoter {

    @Override
    public String quoteTableName(DatabaseMetaData databaseMetaData, String tableName) throws SQLException {
        String[] tableNameItems = tableName.split("\\.");
        String[] quotedTableNames = Arrays.stream(tableNameItems).map(FunctionWithThrowable
                .castFunctionWithThrowable(item -> quoteName(databaseMetaData, item))).toArray(String[]::new);
        return String.join(".", quotedTableNames);
    }

    @Override
    public String quoteColumnName(DatabaseMetaData databaseMetaData, String columnName) throws SQLException {
        return quoteName(databaseMetaData, columnName);
    }

    @Override
    public String[] quoteColumnNames(DatabaseMetaData databaseMetaData, String[] columnNames) throws SQLException {
        String[] quotedColumnNames = Arrays.stream(columnNames).map(FunctionWithThrowable
                .castFunctionWithThrowable(column -> quoteName(databaseMetaData, column))).toArray(String[]::new);
        return quotedColumnNames;
    }

    @Override
    public String[] quoteValues(Object... values) {
        List<Object> quotedValues = new ArrayList<>();

        for (Object value : values) {
            quotedValues.add(quoteValue(value));
        }

        return quotedValues.toArray(new String[0]);
    }

    @Override
    public String quoteValue(Object value) {
        if (value instanceof Integer || value instanceof Long ||
                value instanceof Float || value instanceof Double)
            return String.valueOf(value);
        else {
            String stringValue = String.valueOf(value);
            if (stringValue.startsWith(NO_QUOTE_PREFIX)) {
                String[] stringValues = stringValue.split(":");
                if(stringValues.length < 2)
                    throw new IllegalArgumentException("'%s' is invalid no quote value");
                return String.join("",
                        Arrays.copyOfRange(stringValues, 1, stringValues.length));
            }
        }

        return String.format("'%s'", value);
    }

    private String quoteName(DatabaseMetaData databaseMetaData, String item) throws SQLException {
        String databaseName = databaseMetaData.getDatabaseProductName();
        if (MySQL.nameEquals(databaseName) || MariaDB.nameEquals(databaseName))
            return String.format("`%s`", item);
        else if (PostgreSQL.nameEquals(databaseName) || SQLite.nameEquals(databaseName))
            return String.format("\"%s\"", item);
        else if (Oracle.nameEquals(databaseName))
            return String.format("\"%s\"", item.toUpperCase());
        return String.format("\"%s\"", item);
    }
}
