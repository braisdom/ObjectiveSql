package com.github.braisdom.funcsql.osql.expression;

import com.github.braisdom.funcsql.osql.Expression;

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
