package com.github.braisdom.objsql.sql;

public interface Expression<T> extends Sqlizable {

    Expression<T> as(String alias);
}
