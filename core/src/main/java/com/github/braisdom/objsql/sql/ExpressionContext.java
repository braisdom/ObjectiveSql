package com.github.braisdom.objsql.sql;

import com.github.braisdom.objsql.DatabaseType;

import java.sql.Timestamp;

public interface ExpressionContext {

    DatabaseType getDatabaseType();

    String getAlias(Dataset dataset, boolean forceCreate);

    String quoteTable(String tableName);

    String quoteColumn(String columnName);

    String quoteString(String stringValue);

    String toTimestamp(Timestamp timestamp);
}
