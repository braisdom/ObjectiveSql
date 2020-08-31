package com.github.braisdom.funcsql.osql;

public interface ExpressionContext {

    String getAlias(Dataset dataset);

    String quote(String quotableString);
}
