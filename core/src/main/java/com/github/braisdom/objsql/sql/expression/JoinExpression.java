package com.github.braisdom.objsql.sql.expression;

import com.github.braisdom.objsql.sql.Dataset;
import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.ExpressionContext;

import java.util.Objects;

public class JoinExpression implements Expression {
    public static final int LEFT_OUTER_JOIN = 1;
    public static final int RIGHT_OUTER_JOIN = 2;
    public static final int INNER_JOIN = 3;
    public static final int FULL_JOIN = 4;

    public final int joinType;
    public final Dataset dataset;
    public final Expression onExpression;

    public JoinExpression(int joinType, Dataset dataset, Expression onExpression) {
        Objects.requireNonNull(dataset, "The dataset cannot be null");
        Objects.requireNonNull(onExpression, "The onExpression cannot be null");
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
        String joinTypeString = null;
        switch (joinType) {
            case LEFT_OUTER_JOIN:
                joinTypeString = "LEFT OUTER JOIN";
                break;
            case RIGHT_OUTER_JOIN:
                joinTypeString = "RIGHT OUTER JOIN";
                break;
            case INNER_JOIN:
                joinTypeString = "FULL JOIN";
                break;
            case FULL_JOIN:
                joinTypeString = "FULL JOIN";
                break;
        }
        return String.format(" %s %s %s ", joinTypeString, dataset.toSql(expressionContext),
                onExpression.toSql(expressionContext));
    }
}
