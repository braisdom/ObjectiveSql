package com.github.braisdom.funcsql.sql.expression;

import com.github.braisdom.funcsql.sql.Column;
import com.github.braisdom.funcsql.sql.Expression;
import com.github.braisdom.funcsql.sql.ExpressionContext;

public class ColumnExpression extends AbstractExpression {

    private final Column column;
    private final Expression expression;

    public ColumnExpression(Column column, Expression expression) {
        this.column = column;
        this.expression = expression;
    }

    @Override
    public String toSql(ExpressionContext expressionContext) {
        return String.format("%s %s", column.toSql(expressionContext), expression.toSql(expressionContext));
    }
}
