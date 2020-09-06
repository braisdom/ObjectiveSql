package com.github.braisdom.objsql.sql.expression;

import com.github.braisdom.objsql.sql.AbstractExpression;
import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.ExpressionContext;

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
