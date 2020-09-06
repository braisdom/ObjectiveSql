package com.github.braisdom.objsql.sql.expression;

import com.github.braisdom.objsql.sql.AbstractExpression;
import com.github.braisdom.objsql.sql.Column;
import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.ExpressionContext;

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
