package com.github.braisdom.jsql.ast;

public class ScalarNode extends Aliasable implements Projectional, Expression, BetweenNode.Operand {
    private Object value;

    public ScalarNode(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
