package com.github.braisdom.funcsql.sql.expression;

import com.github.braisdom.funcsql.sql.Expression;
import com.github.braisdom.funcsql.sql.SQLContext;

import java.util.Arrays;
import java.util.List;

public class PolynaryExpression extends AbstractExpression {

    public static final String PLUS = " + ";
    public static final String MINUS = " - ";
    public static final String MULTIPLY = " * ";
    public static final String DIVIDE = " / ";
    public static final String AND = " and ";
    public static final String OR = " or ";
    public static final String LT = " < ";
    public static final String LE = " <= ";
    public static final String GT = " > ";
    public static final String GE = " >= ";
    public static final String NE = " <> ";
    public static final String NE2 = " != ";
    public static final String EQ = " = ";

    private final String operator;
    private final Expression left;
    private final Expression right;
    private final Expression[] others;

    public PolynaryExpression(String operator, Expression left, Expression right, Expression... others) {
        this.operator = operator;
        this.left = left;
        this.right = right;
        this.others = others;
    }

    @Override
    public String toSql(SQLContext sqlContext) {
        List<Expression> expressions = Arrays.asList(new Expression[]{left, right});
        expressions.addAll(Arrays.asList(others));

        String[] expressionStrings = expressions.stream()
                .map(expression -> expression.toSql(sqlContext)).toArray(String[]::new);
        return String.join(operator, expressionStrings);
    }
}
