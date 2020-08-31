package com.github.braisdom.funcsql.osql.expression;

import com.github.braisdom.funcsql.osql.Column;
import com.github.braisdom.funcsql.osql.Expression;
import com.github.braisdom.funcsql.osql.ExpressionContext;

public class ColumnExpression extends AbstractExpression {

    private final Column column;
    private final Expression expression;

    public ColumnExpression(Column column, Expression expression) {
        this.column = column;
        this.expression = expression;
    }

    @Override
    public String toSql(ExpressionContext expressionContext) {
        return String.format(" %s %s ", column.toSql(expressionContext), expression.toSql(expressionContext));
    }
}
