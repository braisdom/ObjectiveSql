package com.github.braisdom.funcsql.sql;

public interface Expression extends Sqlizable {

    Expression as(String alias);
}
