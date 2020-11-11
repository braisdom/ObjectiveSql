package com.github.braisdom.objsql.sql.expression;

import com.github.braisdom.objsql.sql.*;

public class LikeException extends AbstractExpression implements LogicalExpression {

    private final Expression left;
    private final Expression right;
    private final boolean negated;

    public LikeException(boolean negated, Expression left, Expression right) {
        this.negated = negated;
        this.left = left;
        this.right = right;
    }

    @Override
    public LogicalExpression and(LogicalExpression logicalExpression) {
        return new PolynaryExpression(PolynaryExpression.AND, this, logicalExpression);
    }

    @Override
    public LogicalExpression or(LogicalExpression logicalExpression) {
        return new PolynaryExpression(PolynaryExpression.OR, this, logicalExpression);
    }

    @Override
    public Expression as(String alias) {
        throw new UnsupportedOperationException("The null expression cannot be aliased");
    }

    @Override
    public String toSql(ExpressionContext expressionContext) throws SQLSyntaxException {
        if (negated) {
            return String.format(" %s NOT LIKE %s ", left.toSql(expressionContext), right.toSql(expressionContext));
        }
        return String.format(" %s LIKE %s ", left.toSql(expressionContext), right.toSql(expressionContext));
    }
}
