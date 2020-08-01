package com.github.braisdom.funcsql;

public interface Quoter {

    String quoteColumn(String columnName);

    String quoteValue(Object... values);
}
