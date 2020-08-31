package com.github.braisdom.funcsql.sql;

public class Join {
    public static final int LEFT_OUTER_JOIN = 1;
    public static final int RIGHT_OUTER_JOIN = 2;
    public static final int INNER_JOIN = 3;
    public static final int FULL_JOIN = 4;

    public final int joinType;
    public final Dataset dataset;
    public final Expression onExpression;

    public Join(int joinType, Dataset dataset, Expression onExpression) {
        this.joinType = joinType;
        this.dataset = dataset;
        this.onExpression = onExpression;
    }
}
