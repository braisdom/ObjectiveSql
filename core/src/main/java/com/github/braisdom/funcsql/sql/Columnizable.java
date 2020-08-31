package com.github.braisdom.funcsql.sql;

import java.util.Collection;

public interface Columnizable extends Sqlizable, Expression {

    Expression asc();

    Expression desc();

    Expression lt(Expression expr);

    Expression gr(Expression expr);

    Expression eq(Expression expr);

    Expression le(Expression expr);

    Expression ge(Expression expr);

    Expression ne(Expression expr);

    Expression in(Collection scalars);

    Expression in(Object... scalars);

    Expression in(Dataset dataset);

    Expression notIn(Collection scalars);

    Expression notIn(Object... scalars);

    Expression notIn(Dataset dataset);

    Expression between(Expression begin, Expression end);

    Expression notBetween(Expression begin, Expression end);

}
