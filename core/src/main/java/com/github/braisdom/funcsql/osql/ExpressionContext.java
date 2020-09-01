package com.github.braisdom.funcsql.osql;

import java.sql.Timestamp;
import java.util.Date;

public interface ExpressionContext {

    String getAlias(Dataset dataset, boolean forceCreate);

    String quoteTable(String tableName);

    String quoteColumn(String columnName);

    String quoteString(String stringValue);

    String toTimestamp(Timestamp timestamp);
}
