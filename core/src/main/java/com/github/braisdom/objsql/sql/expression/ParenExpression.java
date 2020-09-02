package com.github.braisdom.objsql.sql.expression;

import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.ExpressionContext;

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
