package com.github.braisdom.funcsql.sql.expression;

import com.github.braisdom.funcsql.sql.Expression;
import com.github.braisdom.funcsql.sql.SQLContext;

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
    public Expression as(SQLContext sqlContext, String alias) {
        throw new UnsupportedOperationException("The in expression cannot be aliased");
    }

    @Override
    public String toSql(SQLContext sqlContext) {
        String[] expressionStrings = expressions.stream()
                .map(expression -> expression.toSql(sqlContext)).toArray(String[]::new);
        return String.format(" %s IN (%s)", negated ? "NOT" : "", String.join(" , ", expressionStrings));
    }
}
