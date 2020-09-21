package com.github.braisdom.objsql.sql.expression;

import com.github.braisdom.objsql.sql.AbstractExpression;
import com.github.braisdom.objsql.sql.ExpressionContext;
import com.github.braisdom.objsql.sql.SQLSyntaxException;

public class TrueExpression extends AbstractExpression {

    @Override
    public String toSql(ExpressionContext expressionContext) throws SQLSyntaxException {
        return " 1 = 1 ";
    }
}
