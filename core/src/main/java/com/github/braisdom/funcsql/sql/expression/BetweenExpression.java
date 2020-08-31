package com.github.braisdom.funcsql.sql.expression;

import com.github.braisdom.funcsql.sql.Expression;
import com.github.braisdom.funcsql.sql.SQLContext;

public class BetweenExpression extends AbstractExpression {

    private final Expression left;
    private final Expression right;

    public BetweenExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toSql(SQLContext sqlContext) {
        return String.format(" BETWEEN %s AND %s ", left.toSql(sqlContext), right.toSql(sqlContext));
    }
}
