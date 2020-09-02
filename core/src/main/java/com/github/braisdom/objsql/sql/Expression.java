package com.github.braisdom.objsql.sql;

public interface Expression extends Sqlizable {

    Expression as(String alias);
}
