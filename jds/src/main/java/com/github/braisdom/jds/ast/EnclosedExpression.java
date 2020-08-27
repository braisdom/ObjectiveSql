package com.github.braisdom.jds.ast;

public class EnclosedExpression {
    private Expression expression;

    public EnclosedExpression(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }
}
