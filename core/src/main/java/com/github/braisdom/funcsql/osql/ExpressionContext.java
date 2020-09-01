package com.github.braisdom.funcsql.osql;

import java.sql.Timestamp;

public interface ExpressionContext {

    String getAlias(Dataset dataset, boolean forceCreate);

    String quoteTable(String tableName);

    String quoteColumn(String columnName);

    String quoteString(String stringValue);

    String toTimestamp(Timestamp timestamp);
}
