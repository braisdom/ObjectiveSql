package com.github.braisdom.objsql.sql.expression;

import com.github.braisdom.objsql.sql.AbstractExpression;
import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.ExpressionContext;
import com.github.braisdom.objsql.sql.SQLSyntaxException;

public class EqualsExpression extends AbstractExpression {

    private final Expression rightExpression;
    private final Expression leftExpression;
    private final boolean negated;

    public EqualsExpression(boolean negated, Expression left, Expression right) {
        this.negated = negated;
        this.leftExpression = left;
        this.rightExpression = right;
    }

    @Override
    public Expression as(String alias) {
        throw new UnsupportedOperationException("The equals expression cannot be aliased");
    }

    @Override
    public String toSql(ExpressionContext expressionContext) throws SQLSyntaxException {
        if (negated) {
            return String.format(" %s <> %s ", leftExpression.toSql(expressionContext),
                    rightExpression.toSql(expressionContext));
        }
        return String.format(" %s = %s ", leftExpression.toSql(expressionContext),
                rightExpression.toSql(expressionContext));
    }
}
