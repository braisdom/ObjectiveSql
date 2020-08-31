package com.github.braisdom.funcsql.sql;

public interface ExpressionContext {

    String getAlias(Dataset dataset);

    String quote(String quotableString);
}
