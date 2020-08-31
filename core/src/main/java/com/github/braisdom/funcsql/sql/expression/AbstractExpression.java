package com.github.braisdom.funcsql.sql.expression;

import com.github.braisdom.funcsql.sql.Expression;
import com.github.braisdom.funcsql.sql.ExpressionContext;

public abstract class AbstractExpression implements Expression {

    @Override
    public Expression as(ExpressionContext expressionContext, String alias) {
        return new DefaultExpression(String.format("(%s) AS %s", toSql(expressionContext), expressionContext.quote(alias)));
    }
}
