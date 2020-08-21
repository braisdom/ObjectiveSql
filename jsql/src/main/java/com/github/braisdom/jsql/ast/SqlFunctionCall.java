package com.github.braisdom.jsql.ast;

public class SqlFunctionCall implements Projectional {
    private String name;
    private String expression;

    public String getName() {
        return name;
    }

    public SqlFunctionCall setName(String name) {
        this.name = name;
        return this;
    }

    public String getExpression() {
        return expression;
    }

    public SqlFunctionCall setExpression(String expression) {
        this.expression = expression;
        return this;
    }
}
