package com.github.braisdom.funcsql.sql;

public interface Sqlizable {

    String toSql(ExpressionContext expressionContext);
}
