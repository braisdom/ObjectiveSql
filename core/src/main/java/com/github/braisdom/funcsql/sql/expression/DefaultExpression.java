package com.github.braisdom.funcsql.sql.expression;

import com.github.braisdom.funcsql.sql.ExpressionContext;

public class DefaultExpression extends AbstractExpression {

    private final Object expression;

    public DefaultExpression(Object expression) {
        this.expression = expression;
    }

    @Override
    public String toSql(ExpressionContext expressionContext) {
        return String.valueOf(expression);
    }
}
