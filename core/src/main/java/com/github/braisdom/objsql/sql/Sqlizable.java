package com.github.braisdom.objsql.sql;

public interface Sqlizable {

    String toSql(ExpressionContext expressionContext);
}
