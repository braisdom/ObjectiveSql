package com.github.braisdom.funcsql.sql.expression;

import com.github.braisdom.funcsql.sql.Expression;
import com.github.braisdom.funcsql.sql.SQLContext;

public class BetweenExpression extends AbstractExpression {

    private final Expression left;
    private final Expression right;
    private final boolean negated;

    public BetweenExpression(boolean negated, Expression left, Expression right) {
        this.negated = negated;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toSql(SQLContext sqlContext) {
        return String.format(" %s BETWEEN %s AND %s ",
                negated ? "NOT" : "", left.toSql(sqlContext), right.toSql(sqlContext));
    }
}
