package com.github.braisdom.funcsql.sql;

public interface Columnizable extends Sqlizable {

    Columnizable as(String alias);
}
