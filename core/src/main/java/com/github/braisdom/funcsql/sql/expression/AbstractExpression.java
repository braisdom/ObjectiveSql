package com.github.braisdom.funcsql.sql.expression;

import com.github.braisdom.funcsql.sql.Expression;
import com.github.braisdom.funcsql.sql.SQLContext;

public abstract class AbstractExpression implements Expression {

    @Override
    public Expression as(SQLContext sqlContext, String alias) {
        return new DefaultExpression(String.format("(%s) AS %s", toSql(sqlContext), sqlContext.quote(alias)));
    }
}
