package com.github.braisdom.funcsql.sql.expression;

import com.github.braisdom.funcsql.sql.Expression;

public abstract class AbstractExpression implements Expression {

    private String alias;

    @Override
    public Expression as(String alias) {
        this.alias = alias;
        return this;
    }

    public String getAlias() {
        return alias;
    }
}
