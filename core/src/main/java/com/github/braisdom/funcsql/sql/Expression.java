package com.github.braisdom.funcsql.sql;

public interface Expression extends Sqlizable {

    Expression as(ExpressionContext expressionContext, String alias);
}
