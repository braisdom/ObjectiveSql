package com.github.braisdom.funcsql.sql;

public interface Column extends Expression {

    Expression asc();

    Expression desc();

    Expression lt(Expression expr);

    Expression gr(Expression expr);

    Expression eq(Expression expr);

    Expression le(Expression expr);

    Expression ge(Expression expr);

    Expression ne(Expression expr);

    Expression in(Expression expression, Expression... others);

    Expression in(Dataset dataset);

    Expression notIn(Expression expression, Expression... others);

    Expression notIn(Dataset dataset);

    Expression between(Expression left, Expression right);

    Expression notBetween(Expression left, Expression right);

}
