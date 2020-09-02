package com.github.braisdom.objsql;

public interface Quoter {

    String quoteColumn(String columnName);

    String quoteValue(Object... values);
}
