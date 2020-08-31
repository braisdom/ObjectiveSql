package com.github.braisdom.funcsql.sql;

public interface SQLContext {

    String getAlias(Dataset dataset);

    String quote(String quotableString);
}
