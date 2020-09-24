package com.github.braisdom.objsql.sql.expression;

import com.github.braisdom.objsql.sql.AbstractExpression;
import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.ExpressionContext;
import com.github.braisdom.objsql.sql.SQLSyntaxException;

public class NullExpression extends AbstractExpression {

    private final Expression expression;
    private final boolean negated;

    public NullExpression(boolean negated, Expression expression) {
        this.negated = negated;
        this.expression = expression;
    }

    @Override
    public Expression as(String alias) {
        throw new UnsupportedOperationException("The null expression cannot be aliased");
    }

    @Override
    public String toSql(ExpressionContext expressionContext) throws SQLSyntaxException {
        if (negated) {
            return String.format(" %s IS NOT NULL ", expression.toSql(expressionContext));
        }
        return String.format(" %s IS NULL ", expression.toSql(expressionContext));
    }
}
