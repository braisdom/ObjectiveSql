package com.github.braisdom.objsql.sql.expression;

import com.github.braisdom.objsql.sql.Expression;

public abstract class AbstractExpression<T> implements Expression<T> {

    private String alias;

    @Override
    public Expression<T> as(String alias) {
        this.alias = alias;
        return this;
    }

    public String getAlias() {
        return alias;
    }
}
