package com.github.braisdom.jsql.ast;

public class SqlFunctionCallNode extends Aliasable implements Projectional {
    private String name;
    private String expression;

    public String getName() {
        return name;
    }

    public SqlFunctionCallNode setName(String name) {
        this.name = name;
        return this;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
