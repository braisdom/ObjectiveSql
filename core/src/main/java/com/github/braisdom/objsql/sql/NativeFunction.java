package com.github.braisdom.objsql.sql;

import com.github.braisdom.objsql.sql.expression.AbstractExpression;

import java.util.Arrays;

public class NativeFunction extends AbstractExpression {

    private final String name;
    private final Expression[] expressions;

    public NativeFunction(String name, Expression... expressions) {
        this.name = name;
        this.expressions = expressions;
    }

    public String getName() {
        return name;
    }

    public Expression[] getExpressions() {
        return expressions;
    }

    @Override
    public String toSql(ExpressionContext expressionContext) {
        String[] expressionStrings = Arrays.stream(expressions)
                .map(expression -> expression.toSql(expressionContext)).toArray(String[]::new);
        String alias = getAlias();
        return String.format("%s(%s) %s", name, String.join(",", expressionStrings),
                alias == null ? "" : " AS " + expressionContext.quoteColumn(alias));
    }
}
