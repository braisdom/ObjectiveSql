package com.github.braisdom.jsql.ast;

import java.util.ArrayList;
import java.util.List;

public class SqlFunctionCallNode extends Aliasable implements Projectional {
    private String name;
    private List<Expression> expressions = new ArrayList<>();

    public String getName() {
        return name;
    }

    public SqlFunctionCallNode setName(String name) {
        this.name = name;
        return this;
    }

    public void addExpression(Expression expression) {
        expressions.add(expression);
    }

    public List<Expression> getExpressions() {
        return expressions;
    }
}
