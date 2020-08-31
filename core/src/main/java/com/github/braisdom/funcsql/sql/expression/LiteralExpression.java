package com.github.braisdom.funcsql.sql.expression;

import com.github.braisdom.funcsql.sql.SQLContext;

public class LiteralExpression extends AbstractExpression {

    private final Object rawLiteral;

    public LiteralExpression(Object rawLiteral) {
        this.rawLiteral = rawLiteral;
    }

    @Override
    public String toSql(SQLContext sqlContext) {
        if(rawLiteral == null)
            return "NULL";
        if(String.class.isAssignableFrom(rawLiteral.getClass()))
            return String.format("'%s'", String.valueOf(rawLiteral));
        return String.valueOf(rawLiteral);
    }
}
