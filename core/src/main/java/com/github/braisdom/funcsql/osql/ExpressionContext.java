package com.github.braisdom.funcsql.osql;

import java.sql.Timestamp;
import java.util.Date;

public interface ExpressionContext {

    /**
     * Returns the alias of the dataset
     * @param dataset
     * @param forceCreate
     * @return
     */
    String getAlias(Dataset dataset, boolean forceCreate);

    String quoteTableName(String tableName);

    String quoteColumnName(String columnName);

    String quoteStringValue(String stringValue);

    String toTimestamp(Timestamp timestamp);

    String toTimestamp(Date date);
}
