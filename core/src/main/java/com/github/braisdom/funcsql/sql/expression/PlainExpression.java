package com.github.braisdom.funcsql.sql.expression;

import com.github.braisdom.funcsql.sql.ExpressionContext;

public class PlainExpression extends AbstractExpression {

    private final Object expression;

    public PlainExpression(Object expression) {
        this.expression = expression;
    }

    @Override
    public String toSql(ExpressionContext expressionContext) {
        return String.valueOf(expression);
    }
}
