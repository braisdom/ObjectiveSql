package com.github.braisdom.funcsql.osql.expression;

import com.github.braisdom.funcsql.osql.Expression;
import com.github.braisdom.funcsql.osql.ExpressionContext;

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
