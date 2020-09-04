package com.github.braisdom.objsql.sql.expression;

import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.ExpressionContext;

public class BetweenExpression<T> extends AbstractExpression<T> {

    private final Expression left;
    private final Expression right;
    private final boolean negated;

    public BetweenExpression(boolean negated, Expression<T> left, Expression<T> right) {
        this.negated = negated;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toSql(ExpressionContext expressionContext) {
        return String.format(" %s BETWEEN %s AND %s ",
                negated ? "NOT" : "", left.toSql(expressionContext), right.toSql(expressionContext));
    }
}
