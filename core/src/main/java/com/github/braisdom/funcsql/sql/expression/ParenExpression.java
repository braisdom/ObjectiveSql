package com.github.braisdom.funcsql.sql.expression;

import com.github.braisdom.funcsql.sql.Expression;
import com.github.braisdom.funcsql.sql.ExpressionContext;

public class ParenExpression extends AbstractExpression {

    private final Expression expression;

    public ParenExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toSql(ExpressionContext expressionContext) {
        return String.format("(%s)", expression.toSql(expressionContext));
    }
}
