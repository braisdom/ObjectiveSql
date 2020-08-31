package com.github.braisdom.funcsql.sql.expression;

import com.github.braisdom.funcsql.sql.Expression;
import com.github.braisdom.funcsql.sql.SQLContext;

public class RelationExpression implements Expression {

    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String MULTIPLY = "*";
    public static final String DIVIDE = "/";

    private final String operator;
    private final Expression left;
    private final Expression right;

    public RelationExpression(String operator, Expression left, Expression right, Expression... others) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public Expression as(SQLContext sqlContext, String alias) {
        return null;
    }

    @Override
    public String toSql(SQLContext sqlContext) {
        return null;
    }
}
