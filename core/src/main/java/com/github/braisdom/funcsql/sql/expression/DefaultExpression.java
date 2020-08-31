package com.github.braisdom.funcsql.sql.expression;

import com.github.braisdom.funcsql.sql.Expression;
import com.github.braisdom.funcsql.sql.SQLContext;

public class DefaultExpression implements Expression {

    private final Object expression;

    public DefaultExpression(Object expression) {
        this.expression = expression;
    }

    @Override
    public Expression as(String alias) {
        return this;
    }

    @Override
    public String toSql(SQLContext sqlContext) {
        return String.valueOf(expression);
    }
}
