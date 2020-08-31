package com.github.braisdom.funcsql.sql.expression;

import com.github.braisdom.funcsql.sql.Expression;
import com.github.braisdom.funcsql.sql.SQLContext;

public class Literal implements Expression {

    private final Object rawLiteral;

    public Literal(Object rawLiteral) {
        this.rawLiteral = rawLiteral;
    }

    @Override
    public Expression as(SQLContext sqlContext, String alias) {
        return new DefaultExpression(String.format("(%s) AS %s", toSql(sqlContext), sqlContext.quote(alias)));
    }

    @Override
    public String toSql(SQLContext sqlContext) {
        if(rawLiteral == null)
            return "null";
        return String.valueOf(rawLiteral);
    }
}
