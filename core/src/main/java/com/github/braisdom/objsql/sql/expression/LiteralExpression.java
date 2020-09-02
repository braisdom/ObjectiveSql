package com.github.braisdom.objsql.sql.expression;

import com.github.braisdom.objsql.sql.ExpressionContext;

public class LiteralExpression extends AbstractExpression {

    private final Object rawLiteral;

    public LiteralExpression(Object rawLiteral) {
        this.rawLiteral = rawLiteral;
    }

    @Override
    public String toSql(ExpressionContext expressionContext) {
        if(rawLiteral == null)
            return " NULL ";
        if(String.class.isAssignableFrom(rawLiteral.getClass()))
            return String.format("'%s'", String.valueOf(rawLiteral));
        return String.valueOf(rawLiteral);
    }
}
