package com.github.braisdom.funcsql.osql.expression;

import com.github.braisdom.funcsql.osql.Dataset;
import com.github.braisdom.funcsql.osql.Expression;
import com.github.braisdom.funcsql.osql.ExpressionContext;

public class JoinExpression implements Expression {
    public static final int LEFT_OUTER_JOIN = 1;
    public static final int RIGHT_OUTER_JOIN = 2;
    public static final int INNER_JOIN = 3;
    public static final int FULL_JOIN = 4;

    public final int joinType;
    public final Dataset dataset;
    public final Expression onExpression;

    public JoinExpression(int joinType, Dataset dataset, Expression onExpression) {
        this.joinType = joinType;
        this.dataset = dataset;
        this.onExpression = onExpression;
    }

    @Override
    public Expression as(String alias) {
        throw new UnsupportedOperationException("The join expression cannot be aliased");
    }

    @Override
    public String toSql(ExpressionContext expressionContext) {
        return null;
    }
}
