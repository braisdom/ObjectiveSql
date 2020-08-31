package com.github.braisdom.funcsql.sql;

public interface Columnizable extends Sqlizable, Expression {

    Expression as(String alias);

    Expression lt(Expression expression);

    Expression gr(Expression expression);

    Expression eq(Expression expression);

    Expression le(Expression expression);

    Expression ge(Expression expression);

    Expression ne(Expression expression);

    Expression in(Expression... expressions);

    Expression between(Expression begin, Expression end);

}
