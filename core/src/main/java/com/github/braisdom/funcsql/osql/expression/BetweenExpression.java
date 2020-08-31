package com.github.braisdom.funcsql.osql.expression;

import com.github.braisdom.funcsql.osql.Expression;
import com.github.braisdom.funcsql.osql.ExpressionContext;

public class BetweenExpression extends AbstractExpression {

    private final Expression left;
    private final Expression right;
    private final boolean negated;

    public BetweenExpression(boolean negated, Expression left, Expression right) {
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
