package com.github.braisdom.objsql.sql.expression;

import com.github.braisdom.objsql.sql.ExpressionContext;

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
