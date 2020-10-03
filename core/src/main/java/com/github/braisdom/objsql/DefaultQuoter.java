package com.github.braisdom.objsql;

import com.github.braisdom.objsql.util.FunctionWithThrowable;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Arrays;

import static com.github.braisdom.objsql.DatabaseType.*;

public class DefaultQuoter implements Quoter{

    @Override
    public String quoteTableName(DatabaseMetaData databaseMetaData, String tableName) throws SQLException {
        String[] tableNameItems = tableName.split("\\.");
        String[] quotedTableNames = Arrays.stream(tableNameItems).map(FunctionWithThrowable
                .castFunctionWithThrowable(item -> quoteItem(databaseMetaData, item))).toArray(String[]::new);
        return String.join(".", quotedTableNames);
    }

    @Override
    public String quoteColumnName(DatabaseMetaData databaseMetaData, String columnName) throws SQLException {
        return quoteItem(databaseMetaData, columnName);
    }

    @Override
    public String[] quoteColumnNames(DatabaseMetaData databaseMetaData, String[] columnNames) throws SQLException {
        String[] quotedColumnNames = Arrays.stream(columnNames).map(FunctionWithThrowable
                .castFunctionWithThrowable(column -> quoteItem(databaseMetaData, column))).toArray(String[]::new);
        return quotedColumnNames;
    }

    @Override
    public String quoteValue(Object... values) {
        StringBuilder sb = new StringBuilder();

        for (Object value : values) {
            if (value instanceof Integer || value instanceof Long ||
                    value instanceof Float || value instanceof Double)
                sb.append(value);
            else {
                sb.append(String.format("'%s'", value));
            }
            sb.append(",");
        }

        if (sb.length() > 0)
            sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }

    private String quoteItem(DatabaseMetaData databaseMetaData, String item) throws SQLException {
        String databaseName = databaseMetaData.getDatabaseProductName();
        if (MySQL.nameEquals(databaseName) || MariaDB.nameEquals(databaseName))
            return String.format("`%s`", item);
        else if (PostgreSQL.nameEquals(databaseName) || Oracle.nameEquals(databaseName)
                || SQLite.nameEquals(databaseName))
            return String.format("\"%s\"", item);
        return String.format("\"%s\"", item);
    }
}
