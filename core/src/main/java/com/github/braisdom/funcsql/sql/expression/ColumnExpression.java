package com.github.braisdom.funcsql.sql.expression;

import com.github.braisdom.funcsql.sql.Column;
import com.github.braisdom.funcsql.sql.Expression;
import com.github.braisdom.funcsql.sql.SQLContext;

public class ColumnExpression extends AbstractExpression {

    private final Column column;
    private final Expression expression;

    public ColumnExpression(Column column, Expression expression) {
        this.column = column;
        this.expression = expression;
    }

    @Override
    public String toSql(SQLContext sqlContext) {
        return String.format("%s %s", column.toSql(sqlContext), expression.toSql(sqlContext));
    }
}
