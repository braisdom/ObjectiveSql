package com.github.braisdom.objsql.sql.expression;

import static com.github.braisdom.objsql.sql.Expressions.$;

public class EternalExpression extends PolynaryExpression {

    public EternalExpression() {
        super(EQ, $(1), $(1));
    }
}
