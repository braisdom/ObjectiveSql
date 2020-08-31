package com.github.braisdom.funcsql.sql;

import com.github.braisdom.funcsql.sql.expression.LiteralExpression;
import com.github.braisdom.funcsql.sql.expression.ParenExpression;
import com.github.braisdom.funcsql.sql.expression.PolynaryExpression;

import java.sql.Timestamp;

public class Expressions {

    public static Expression paren(Expression expression) {
        return new ParenExpression(expression);
    }

    public static Expression plus(Expression left, Expression right, Expression... others) {
        return new PolynaryExpression(PolynaryExpression.PLUS, left, right, others);
    }

    public static Expression minus(Expression left, Expression right, Expression... others) {
        return new PolynaryExpression(PolynaryExpression.MINUS, left, right, others);
    }

    public static Expression multiply(Expression left, Expression right, Expression... others) {
        return new PolynaryExpression(PolynaryExpression.MULTIPLY, left, right, others);
    }

    public static Expression divide(Expression left, Expression right, Expression... others) {
        return new PolynaryExpression(PolynaryExpression.DIVIDE, left, right, others);
    }

    public static Expression and(Expression left, Expression right, Expression... others) {
        return new PolynaryExpression(PolynaryExpression.AND, left, right, others);
    }

    public static Expression or(Expression left, Expression right, Expression... others) {
        return new PolynaryExpression(PolynaryExpression.OR, left, right, others);
    }

    public static Expression literal(String string) {
        return new LiteralExpression(string);
    }

    public static Expression literal(Integer integer) {
        return new LiteralExpression(integer);
    }

    public static Expression literal(Float floatLiteral) {
        return new LiteralExpression(floatLiteral);
    }

    public static Expression literal(Double doubleLiteral) {
        return new LiteralExpression(doubleLiteral);
    }

    public static Expression literal(Timestamp timestamp) {
        return null;
    }
}
