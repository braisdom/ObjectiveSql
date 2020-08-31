package com.github.braisdom.funcsql.sql.expression;

import com.github.braisdom.funcsql.sql.Expression;
import com.github.braisdom.funcsql.sql.ExpressionContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InExpression extends AbstractExpression {

    private final List<Expression> expressions = new ArrayList<>();

    private final boolean negated;

    public InExpression(boolean negated, Expression expression, Expression... others) {
        this.negated = negated;
        expressions.add(expression);
        expressions.addAll(Arrays.asList(others));
    }

    @Override
    public Expression as(ExpressionContext expressionContext, String alias) {
        throw new UnsupportedOperationException("The in expression cannot be aliased");
    }

    @Override
    public String toSql(ExpressionContext expressionContext) {
        String[] expressionStrings = expressions.stream()
                .map(expression -> expression.toSql(expressionContext)).toArray(String[]::new);
        return String.format(" %s IN (%s)", negated ? "NOT" : "", String.join(" , ", expressionStrings));
    }
}
